package com.numble.team3.converter.infra;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.converter.application.request.CreateImageDto;
import com.numble.team3.converter.domain.ConvertImageUtils;
import com.numble.team3.exception.convert.ImageConvertFailureException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsConvertImageUtils implements ConvertImageUtils {

  @Value("${cloud.aws.image.s3.name}")
  private String bucket;

  @Value("${cloud.aws.image.api-gateway.url}")
  private String apiGateway;

  private final AmazonS3Client amazonS3Client;
  private final ObjectMapper objectMapper;

  @Override
  public String processImageResize(String filename, CreateImageDto dto) {
    Map<String, String> params = new HashMap<>(){
      {
        put("filename", filename);
        put("width", dto.getWidth());
        put("height", dto.getHeight());
        put("type", dto.getType());
      }
    };

    String url = "";

    try {
      url = requestApiGateway("images", params);
    } catch (Exception e) {
      log.error("", e);
    }

    return url;
  }

  @Override
  public void saveTempImageFileForMetadata(String filename, CreateImageDto dto) throws IOException {
    amazonS3Client.putObject(bucket, filename, dto.getFile().getInputStream(), generateObjectMetaData(dto.getFile()));
  }

  private String requestApiGateway(String url, Map<String, String> params) {
    WebClient webClient = WebClient.builder()
      .baseUrl(apiGateway)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();

    String result = webClient.post()
      .uri(url)
      .bodyValue(params)
      .retrieve()
      .bodyToMono(String.class).block();

    try {
      Map<String, String> resultMap = objectMapper.readValue(result, Map.class);
      return resultMap.get("url");
    } catch (Exception e) {
      throw new ImageConvertFailureException();
    }
  }

  private ObjectMetadata generateObjectMetaData(MultipartFile file) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());
    return objectMetadata;
  }
}
