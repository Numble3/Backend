package com.numble.team3.converter.application;

import com.numble.team3.converter.domain.ConvertImageUtils;
import com.numble.team3.exception.convert.ImageConvertFailureException;
import com.numble.team3.exception.convert.ImageTypeUnSupportException;
import com.numble.team3.converter.application.request.CreateImageDto;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiGatewayImageConvertService implements ImageConvertService {

  private static final List<String> IMAGE_TYPE = List.of("jpeg", "png", "jpg");

  private final ConvertImageUtils imageUtils;

  @Override
  public String uploadResizeImage(CreateImageDto dto) {
    String filename = createImageFilename(dto.getFile().getOriginalFilename());

    try {
      imageUtils.saveTempImageFileForMetadata(filename, dto);
    } catch (IOException e) {
      throw new ImageConvertFailureException();
    }

    return imageUtils.processImageResize(filename, dto);
  }

  private String createImageFilename(String originalFilename) {
    String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

    if (!(IMAGE_TYPE.contains(ext))) {
      throw new ImageTypeUnSupportException();
    }

    return UUID.randomUUID().toString().substring(0, 10) + "." + ext;
  }
}
