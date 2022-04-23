package com.numble.team3.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final static String TYPE = "Bearer ";

  public String createToken(String key, Map<String, Object> privateClaims, long maxAgeSeconds) {
    Date now = new Date();

    return TYPE + Jwts.builder()
      .setIssuedAt(now)
      .setExpiration(new Date(now.getTime() + maxAgeSeconds * 1000L))
      .addClaims(privateClaims)
      .signWith(SignatureAlgorithm.HS256, key.getBytes())
      .compact();
  }

  public Optional<Claims> parse(String key, String token) {
    try {
      return Optional.of(
        Jwts.parser().setSigningKey(key.getBytes()).parseClaimsJws(untype(token)).getBody());
    } catch (JwtException e) {
      return Optional.empty();
    }
  }

  private String untype(String token) {
    return token.substring(TYPE.length());
  }
}
