package com.numble.team3.video.infra;

import com.numble.team3.video.domain.VideoUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoRedisUtils implements VideoUtils {
  private final RedisTemplate redisTemplate;
  private final String PREFIX_VIDEO_VIEW = "VIDEO::VIEW::";

  @Override
  public void updateViewCount(Long videoId) {
    redisTemplate.opsForValue().increment(PREFIX_VIDEO_VIEW + String.valueOf(videoId));
  }

  public Map<Long, Long> getAllVideoViewCount(){
    List<String> keys = (List<String>) redisTemplate.keys(PREFIX_VIDEO_VIEW + "*").stream().collect(Collectors.toList());
    List<String> viewCounts = redisTemplate.opsForValue().multiGet(keys);
    redisTemplate.delete(keys);

    Map<Long, Long> result = new HashMap<>();
    for(int i = 0; i < keys.size(); i++){
      result.put(Long.valueOf(keys.get(i).substring(PREFIX_VIDEO_VIEW.length())), Long.valueOf(viewCounts.get(i)));
    }
    return result;
  }
}
