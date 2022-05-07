package com.numble.team3.video.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.video.domain.VideoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoRedisUtils implements VideoUtils {
  private final RedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;
  private final String PREFIX_VIDEO_VIEW = "VIDEO::VIEW::";

  @Override
  public Long getViewCountByVideoId(Long videoId) {
    if(!redisTemplate.hasKey(PREFIX_VIDEO_VIEW + String.valueOf(videoId))){
      return 0L;
    }
    return Long.valueOf((String) redisTemplate.opsForValue().get(PREFIX_VIDEO_VIEW + String.valueOf(videoId)));
  }

  @Override
  public void countView(Long curViewCount, Long videoId) {
    redisTemplate.opsForValue().set(PREFIX_VIDEO_VIEW + String.valueOf(videoId), String.valueOf(curViewCount));
  }
}
