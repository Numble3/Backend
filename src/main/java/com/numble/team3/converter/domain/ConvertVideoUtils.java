package com.numble.team3.converter.domain;

import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
import java.io.IOException;

public interface ConvertVideoUtils {
  String saveTempVideoForConvert(String filname, CreateVideoDto dto) throws IOException;

  long processConvertVideo(String dirName, String filename) throws IOException;
}
