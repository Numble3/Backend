package com.numble.team3.video.domain;

import java.util.Map;

public interface VideoUtils {
  void updateViewCount(Long videoId);

  Map<Long, Long> getAllVideoViewCount();
}
