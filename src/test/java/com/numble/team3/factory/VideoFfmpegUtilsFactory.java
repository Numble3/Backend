package com.numble.team3.factory;

import static org.springframework.test.util.ReflectionTestUtils.setField;
import com.numble.team3.converter.infra.VideoFfmpegUtils;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;

public class VideoFfmpegUtilsFactory {
  public static VideoFfmpegUtils createVideoFfmpegUtils(FFmpeg fFmpeg, FFprobe fFprobe){
    VideoFfmpegUtils videoFfmpegUtils = new VideoFfmpegUtils();
    setField(videoFfmpegUtils, "ffmpeg", fFmpeg);
    setField(videoFfmpegUtils, "ffprobe", fFprobe);
    return videoFfmpegUtils;
  }
}
