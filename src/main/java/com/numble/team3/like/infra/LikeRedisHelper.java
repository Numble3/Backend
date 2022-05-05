package com.numble.team3.like.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.like.application.response.GetVideoRankDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeRedisHelper {

  private final RedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  private static final String PREFIX_KEY = "video::ranking::";

  public void saveRanking(List<GetVideoRankDto> ranking, String standard) {
    String key = PREFIX_KEY + standard;

    ranking.stream().forEach(rank -> {
      try {
        redisTemplate.opsForList().rightPush(key, objectMapper.writeValueAsString(rank));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    });
  }

  public List<GetVideoRankDto> getRanking(String standard) {
    String key = PREFIX_KEY + standard;

    return ((List<Object>) redisTemplate.opsForList().range(key, 0, 49)).stream().map(video -> {
      try {
        return objectMapper.readValue((String) video, GetVideoRankDto.class);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }).collect(Collectors.toList());
  }

  public void deleteRanking(String standard) {
    String key = PREFIX_KEY + standard;

    redisTemplate.delete(key);
  }
}