package com.numble.team3.converter.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertUrlDto;
import com.numble.team3.exception.convert.ImageConvertFailureException;
import com.numble.team3.exception.convert.ImageTypeUnSupportException;
import com.numble.team3.converter.application.request.CreateImageDto;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ApiGatewayConvertService implements ConvertService {

  private static final List<String> IMAGE_TYPE = List.of("jpeg", "png", "jpg");
  private static final List<String> VIDEO_TYPE = List.of("mp4");

  @Value("${cloud.aws.image.s3.name}")
  private String imageBucket;

  @Value("${cloud.aws.video.s3.name}")
  private String videoBucket;

  @Value("${cloud.aws.image.api-gateway.url}")
  private String imageApiGateway;

  @Value("${cloud.aws.video.api-gateway.url}")
  private String videoApiGateway;

  private final Long VIDEO_CHUNK_UNIT = 10L; // 10초 단위로 분리

  private final AmazonS3Client amazonS3Client;

  @Override
  public String uploadResizeImage(CreateImageDto dto) throws IOException {
    String filename = createImageFilename(dto.getFile().getOriginalFilename());
    ObjectMetadata objectMetadata = generateObjectMetaData(dto.getFile());

    amazonS3Client.putObject(imageBucket, filename, dto.getFile().getInputStream(), objectMetadata);

    return processImageApiGateway(filename, dto.getWidth(), dto.getHeight(), dto.getType());
  }

  @Override
  public GetConvertUrlDto uploadConvertVideo(CreateVideoDto dto) throws IOException {
    String filename = createVideoFilename(dto.getFile().getOriginalFilename());
    amazonS3Client.putObject(videoBucket, filename, dto.getFile().getOriginalFilename());
    return new GetConvertUrlDto(processVideoApiGateway(filename));
  }

  private String createImageFilename(String originalFilename) {
    String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

    if (!(IMAGE_TYPE.contains(ext))) {
      throw new ImageTypeUnSupportException();
    }

    return UUID.randomUUID().toString().substring(0, 10) + "." + ext;
  }

  private String createVideoFilename(String originalFilename) {
    String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

    if (!(VIDEO_TYPE.contains(ext))) {
      throw new ImageTypeUnSupportException();
    }

    return UUID.randomUUID().toString().substring(0, 10) + "." + ext;
  }

  private String processVideoApiGateway(String filename) {
    WebClient webClient =
        WebClient.builder()
            .baseUrl(videoApiGateway)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    String result =
        webClient
            .post()
            .uri("/videos")
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
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      Map<String, String> resultMap = objectMapper.readValue(result, Map.class);
      return resultMap.get("url");
    } catch (Exception e) {
      throw new ImageConvertFailureException();
    }
  }

  private String processImageApiGateway(String filename, String width, String height, String type) {
    WebClient webClient =
        WebClient.builder()
            .baseUrl(imageApiGateway)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    String result =
        webClient
            .post()
            .uri("/images")
            .bodyValue(
                new HashMap<String, String>() {
                  {
                    put("filename", filename);
                    put("width", width);
                    put("height", height);
                    put("type", type);
                  }
                })
            .retrieve()
            .bodyToMono(String.class)
            .block();

    ObjectMapper objectMapper = new ObjectMapper();
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
