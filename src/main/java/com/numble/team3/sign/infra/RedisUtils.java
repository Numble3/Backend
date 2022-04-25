package com.numble.team3.sign.infra;

import com.numble.team3.jwt.TokenHelper;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtils {

  private final RedisTemplate redisTemplate;
  private final TokenHelper accessTokenHelper;
  private final TokenHelper refreshTokenHelper;

  private static final String SEPARATOR = "::";

  public void saveAccessToken(Long id, String accessToken) {
    redisTemplate.opsForValue().set("accessToken" + SEPARATOR + id, accessToken,
      accessTokenHelper.getExpiration(accessToken), TimeUnit.MILLISECONDS);
  }

  public void saveRefreshToken(Long id, String refreshToken) {
    redisTemplate.opsForValue().set("refreshToken" + SEPARATOR + id, refreshToken,
      refreshTokenHelper.getExpiration(refreshToken), TimeUnit.MILLISECONDS);
  }

  public boolean validToken(String token, String key, Optional<String> id) {
    return token.equals(getToken(key, id.map(accountId -> Long.valueOf(accountId)).orElse(null)));
  }

  public String getToken(String key, Long id) {
    return (String) redisTemplate.opsForValue().get(key + SEPARATOR + id);
  }

  public void deleteToken(String key, Long id) {
    redisTemplate.delete(key + SEPARATOR + id);
  }
}
