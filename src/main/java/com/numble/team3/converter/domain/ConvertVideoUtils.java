package com.numble.team3.converter.domain;

import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ConvertVideoUtils {
  String saveTempVideoForConvert(String dirFullPath, MultipartFile videoFile) throws IOException;

  ConvertResult processConvertVideo(String dirFullPath, String fileFullPath) throws IOException;

  String getFileOriginName(String filePath);
  String getFileExt(String filePath);
}
