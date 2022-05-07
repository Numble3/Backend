package com.numble.team3.sign.infra;

import com.numble.team3.account.infra.AccountRedisHelper;
import com.numble.team3.exception.sign.TokenFailureException;
import com.numble.team3.jwt.PrivateClaims;
import com.numble.team3.sign.domain.SignUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSignUtils implements SignUtils {

  private final SignRedisHelper signRedisHelper;
  private final AccountRedisHelper accountRedisHelper;

  private final static String ACCESS_TOKEN_KEY = "accessToken";
  private final static String REFRESH_TOKEN_KEY = "refreshToken";

  @Override
  public void processSignIn(Long accountId, String accessToken, String refreshToken) {
    signRedisHelper.saveAccessToken(accountId, accessToken);
    signRedisHelper.saveRefreshToken(accountId, refreshToken);
    accountRedisHelper.saveLastLogin(accountId);
  }

  @Override
  public void validationRefreshToken(PrivateClaims privateClaims, String refreshToken) {
    if (!signRedisHelper.validToken(
      refreshToken, REFRESH_TOKEN_KEY, Optional.ofNullable(privateClaims.getAccountId()))) {
      throw new TokenFailureException();
    }
  }

  @Override
  public void changeAccessToken(PrivateClaims privateClaims, String accessToken) {
    Long accountId = Long.valueOf(privateClaims.getAccountId());

    signRedisHelper.deleteToken(ACCESS_TOKEN_KEY, accountId);
    signRedisHelper.saveAccessToken(accountId, accessToken);
  }

  @Override
  public void deleteToken(Long accountId) {
    signRedisHelper.deleteToken(REFRESH_TOKEN_KEY, accountId);
    signRedisHelper.deleteToken(ACCESS_TOKEN_KEY, accountId);
  }

  @Override
  public void processWithdrawal(Long accountId) {
    signRedisHelper.deleteToken(REFRESH_TOKEN_KEY, accountId);
    signRedisHelper.deleteToken(ACCESS_TOKEN_KEY, accountId);
  }
}
