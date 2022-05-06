package com.numble.team3.converter.application;

import com.numble.team3.converter.application.request.CreateImageDto;
import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
import java.io.IOException;

public interface ConvertService {

  String uploadResizeImage(CreateImageDto dto) throws IOException;
  GetConvertVideoDto uploadConvertVideo(CreateVideoDto dto) throws IOException;
}
