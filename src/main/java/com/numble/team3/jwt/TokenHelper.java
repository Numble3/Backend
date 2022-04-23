package com.numble.team3.jwt;

import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenHelper {

  private final JwtProvider jwtProvider;
  private final String key;
  private final long maxAgeSeconds;

  private static final String SEPARATOR = ",";
  private static final String ROLE_TYPES = "ROLE_TYPES";
  private static final String ACCOUNT_ID = "ACCOUNT_ID";

  public String createToken(PrivateClaims privateClaims) {
    return jwtProvider.createToken(key,
      Map.of(
        ACCOUNT_ID, privateClaims.getAccountId(),
        ROLE_TYPES, privateClaims.getRoleTypes().stream().collect(Collectors.joining(SEPARATOR))),
      maxAgeSeconds);
  }

  public Optional<PrivateClaims> parse(String token) {
    return jwtProvider.parse(key, token).map(this::convert);
  }

  public Long getExpiration(String token) {
    Long expiration = jwtProvider.parse(key, token).map(claims -> claims.getExpiration().getTime())
      .orElseThrow(RuntimeException::new);

    return expiration - LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()
      .toEpochMilli();
  }

  private PrivateClaims convert(Claims claims) {
    return new PrivateClaims(
      claims.get(ACCOUNT_ID, String.class),
      Arrays.asList(claims.get(ROLE_TYPES, String.class).split(SEPARATOR))
    );
  }
}
