package com.numble.team3.factory.dto;

import com.numble.team3.converter.application.request.CreateImageDto;
import org.springframework.web.multipart.MultipartFile;

public class ImageDtoFactory {

  public static CreateImageDto createImageDto(MultipartFile file, String width, String height, String type) {
    CreateImageDto dto = new CreateImageDto();

    dto.setFile(file);
    dto.setWidth(width);
    dto.setHeight(height);
    dto.setType(type);

    return dto;
  }
}