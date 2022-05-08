package com.numble.team3.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.numble.team3.account.domain.RoleType;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("TokenHelper 테스트")
public class TokenHelperTest {

  TokenHelper tokenHelper = new TokenHelper(new JwtProvider(), "myKey", 60L);
  PrivateClaims privateClaims = new PrivateClaims(String.valueOf(1L), List.of(RoleType.ROLE_USER.toString()));

  @Test
  void createToken_테스트() {
    // given

    // when
    String token = tokenHelper.createToken(privateClaims);

    // then
    assertTrue(token.contains("Bearer "));
  }

  @Test
  void parse_성공_테스트() {
    // given
    String token = tokenHelper.createToken(privateClaims);

    // when
    PrivateClaims privateClaims = tokenHelper.parse(token).orElse(null);

    // then
    assertNotNull(privateClaims);
    assertEquals(1L, Long.valueOf(privateClaims.getAccountId()));
    assertEquals("ROLE_USER", privateClaims.getRoleTypes().get(0).toString());
  }

  @Test
  void parse_실패_테스트() {
    // given
    String token = "유효하지 않은 토큰";

    // when
    Optional<PrivateClaims> parse = tokenHelper.parse(token);

    // then
    assertEquals(Optional.empty(), parse);
  }
}