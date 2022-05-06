package com.numble.team3.converter.domain;

import com.numble.team3.converter.application.request.CreateImageDto;
import java.io.IOException;

public interface ConvertImageUtils {

  String processImageResize(String filename, CreateImageDto dto);

  void saveTempImageFileForMetadata(String filename, CreateImageDto dto) throws IOException;
}
