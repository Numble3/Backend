package com.numble.team3.converter.domain;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ConvertVideoUtils {
  String saveTempVideoForConvert(String dirFullPath, MultipartFile videoFile) throws IOException;

  ConvertResult processConvertVideo(String dirFullPath, String fileFullPath) throws IOException;

  String getFileOriginName(String filePath);
  String getFileExt(String filePath);
}
