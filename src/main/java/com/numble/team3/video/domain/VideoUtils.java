package com.numble.team3.video.domain;

public interface VideoUtils {
  Long getViewCountByVideoId(Long VideoId);

  void countView(Long curViewCount, Long videoId);
}
