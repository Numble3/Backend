package com.numble.team3.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
public class LocalTestRedisClientConfig {

  @Bean
  @Profile("test")
  public RedisConnectionFactory redisConnectionFactory(){
    return new LettuceConnectionFactory("127.0.0.1", 6378);
  }
}
