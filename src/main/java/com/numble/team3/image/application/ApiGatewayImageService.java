package com.numble.team3.image.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.exception.image.ImageConvertFailureException;
import com.numble.team3.exception.image.ImageTypeUnSupportException;
import com.numble.team3.image.application.request.CreateImageDto;
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
public class ApiGatewayImageService implements ImageService {

  private static final List<String> IMAGE_TYPE = List.of("jpeg", "png", "jpg");

  @Value("${cloud.aws.image.s3.name}")
  private String bucket;

  @Value("${cloud.aws.image.api-gateway.url}")
  private String apiGateway;

  private final AmazonS3Client amazonS3Client;

  @Override
  public String uploadResizeImage(CreateImageDto dto) throws IOException {
    String filename = createFilename(dto.getFile().getOriginalFilename());
    ObjectMetadata objectMetadata = generateObjectMetaData(dto.getFile());

    amazonS3Client.putObject(bucket, filename, dto.getFile().getInputStream(), objectMetadata);

    return processApiGateway(filename, dto.getWidth(), dto.getHeight(), dto.getType());
  }

  private String createFilename(String originalFilename) {
    String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

    if (!(IMAGE_TYPE.contains(ext))) {
      throw new ImageTypeUnSupportException();
    }

    return UUID.randomUUID().toString().substring(0, 10) + "." + ext;
  }

  private String processApiGateway(String filename, String width, String height, String type) {
    WebClient webClient = WebClient.builder()
      .baseUrl(apiGateway)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();

    String result = webClient.post()
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
      .bodyToMono(String.class).block();

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
