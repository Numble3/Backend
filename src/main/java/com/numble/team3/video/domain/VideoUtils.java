package com.numble.team3.video.domain;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface VideoUtils {
  String convertVideo(MultipartFile videoFile);
  long extractVideoDuration(String videoUrl);
}
