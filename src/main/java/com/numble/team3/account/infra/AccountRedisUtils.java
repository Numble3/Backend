package com.numble.team3.account.infra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRedisUtils {

  private final RedisTemplate redisTemplate;
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

  private static final String SEPARATOR = "::";

  public void saveLastLogin(Long id) {
    redisTemplate.opsForValue()
      .set("lastLogin" + SEPARATOR + id, formatter.format(LocalDateTime.now()));
  }

  public void deleteLastLogin(Long id) {
    redisTemplate.delete("lastLogin" + SEPARATOR + id);
  }

  public Optional<String> getLastLogin(Long id) {
    return Optional.ofNullable(
      (String) redisTemplate.opsForValue().get("lastLogin" + SEPARATOR + id));
  }

  public List<Long> getAllLastLoginKey() {
    Set<String> keys = redisTemplate.keys("lastLogin::*");

    return keys.stream().map(key -> Long.valueOf(key.split("::")[1])).collect(Collectors.toList());
  }

}
