package com.numble.team3.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("JwtProvider 테스트")
public class JwtProviderTest {

  JwtProvider jwtProvider = new JwtProvider();

  @Test
  void createToken_테스트() {
    // given
    String tokenKey = "tokenKey";
    Map<String, Object> privateClaims = new HashMap<>() {
      {
        put("ACCOUNT_ID", 1L);
        put("ROLE_TYPES", "USER");
      }
    };
    long maxAgeSeconds = 60L;

    // when
    String token = jwtProvider.createToken(tokenKey, privateClaims, maxAgeSeconds);

    // then
    assertTrue(token.contains("Bearer"));
  }

  @Test
  void parse_성공_테스트() {
    // given
    String tokenKey = "tokenKey";
    Map<String, Object> privateClaims = new HashMap<>() {
      {
        put("ACCOUNT_ID", 1L);
        put("ROLE_TYPES", "USER");
      }
    };
    long maxAgeSeconds = 60L;

    String token = jwtProvider.createToken(tokenKey, privateClaims, maxAgeSeconds);

    // when
    Optional<Claims> parse = jwtProvider.parse(tokenKey, token);

    // then
    assertEquals(1L,
      Long.valueOf(String.valueOf(parse.map(claims -> claims.get("ACCOUNT_ID")).orElseThrow(RuntimeException::new))));
    assertEquals("USER",
      String.valueOf(parse.map(claims -> claims.get("ROLE_TYPES")).orElseThrow(RuntimeException::new)));
  }

  @Test
  void parse_실패_테스트() {
    // given
    String tokenKey = "tokenKey";
    Map<String, Object> privateClaims = new HashMap<>() {
      {
        put("ACCOUNT_ID", 1L);
        put("ROLE_TYPES", "USER");
      }
    };
    long maxAgeSeconds = 60L;

    String token = jwtProvider.createToken(tokenKey, privateClaims, maxAgeSeconds);

    // when
    Optional<Claims> parse = jwtProvider.parse(tokenKey, "유효하지 않은 토큰");

    // then
    assertEquals(Optional.empty(), parse);
  }
}