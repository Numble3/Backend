package com.numble.team3.converter.domain;

import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
import java.io.IOException;

public interface ConvertVideoUtils {
  void saveTempVideoForConvert(String filname, CreateVideoDto dto) throws IOException;

  GetConvertVideoDto processConvertVideo(String filename) throws IOException;
}
