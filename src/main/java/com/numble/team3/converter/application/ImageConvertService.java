package com.numble.team3.converter.application;

import com.numble.team3.converter.application.request.CreateImageDto;

public interface ImageConvertService {
  String uploadResizeImage(CreateImageDto dto);
}
