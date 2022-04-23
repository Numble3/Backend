package com.numble.team3.config;

import com.numble.team3.jwt.JwtProvider;
import com.numble.team3.jwt.TokenHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeanConfig {

  @Bean
  public TokenHelper accessTokenHelper(
    JwtProvider jwtProvider,
    @Value("${jwt.key.access}") String key,
    @Value("${jwt.max-age.access}") long maxAgeSeconds) {
    return new TokenHelper(jwtProvider, key, maxAgeSeconds);
  }

  @Bean
  public TokenHelper refreshTokenHelper(
    JwtProvider jwtProvider,
    @Value("${jwt.key.refresh}") String key,
    @Value("${jwt.max-age.refresh}") long maxAgeSeconds) {
    return new TokenHelper(jwtProvider, key, maxAgeSeconds);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
