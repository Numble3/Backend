package com.numble.team3.video.infra;

import com.numble.team3.video.domain.VideoUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class VideoFfmpegUtils implements VideoUtils {

  @Override
  public String convertVideo(MultipartFile videoFile) {
    return null;
  }

  @Override
  public long extractVideoDuration(String videoUrl) {
    return 1L;
  }
}
