package com.numble.team3.video.domain;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface VideoUtils {
  int VIDEO_CHUNK_UNIT = 10;
  String convertVideo(MultipartFile videoFile) throws IOException;
  long extractVideoDuration(String videoUrl) throws IOException;
}
