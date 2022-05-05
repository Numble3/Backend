package com.numble.team3.security;

import com.numble.team3.sign.infra.SignRedisHelper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityRedisUtils implements SecurityUtils {

  private final SignRedisHelper signRedisHelper;

  @Override
  public boolean validToken(String token, String key, Optional<String> id) {
    return signRedisHelper.validToken(token, key, id);
  }

  @Override
  public void oauth2ProcessAccount(Long accountId, String accessToken, String refreshToken) {
    signRedisHelper.saveAccessToken(accountId, accessToken);
    signRedisHelper.saveRefreshToken(accountId, refreshToken);
  }
}
