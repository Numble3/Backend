package com.numble.team3.converter.infra;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
import com.numble.team3.converter.domain.ConvertVideoUtils;
import com.numble.team3.exception.convert.VideoConvertFailureException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AwsConvertVideoUtils implements ConvertVideoUtils {
  @Value("${cloud.aws.video.s3.name}")
  private String bucket;

  @Value("${cloud.aws.video.api-gateway.url}")
  private String apiGateway;

  private final AmazonS3Client amazonS3Client;
  private final ObjectMapper objectMapper;
  private final long VIDEO_CHUNK_UNIT = 10;

  @Override
  public void saveTempVideoForConvert(String filname, CreateVideoDto dto) throws IOException {
    amazonS3Client.putObject(
        bucket, filname, dto.getFile().getInputStream(), generateObjectMetaData(dto.getFile()));
  }

  @Override
  public GetConvertVideoDto processConvertVideo(String filename) {
    WebClient webClient =
        WebClient.builder()
            .baseUrl(apiGateway)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    String result =
        webClient
            .post()
            .uri("/videos/storage")
            .bodyValue(
                new HashMap<String, String>() {
                  {
                    put("filename", filename);
                    put("chunk_unit", String.valueOf(VIDEO_CHUNK_UNIT));
                  }
                })
            .retrieve()
            .bodyToMono(String.class)
            .block();
    try {
      Map<String, String> resultMap = objectMapper.readValue(result, Map.class);
      return new GetConvertVideoDto(resultMap.get("url"), Long.valueOf(resultMap.get("duration")));
    } catch (Exception e) {
      throw new VideoConvertFailureException();
    }
  }

  private ObjectMetadata generateObjectMetaData(MultipartFile file) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());
    return objectMetadata;
  }
}
