package com.numble.team3.sign.domain;

import com.numble.team3.jwt.PrivateClaims;

public interface SignUtils {

  void processSignInRedis(Long accountId, String accessToken, String refreshToken);

  void processRefreshValidationRedis(PrivateClaims privateClaims, String refreshToken);

  void processChangeAccessTokenRedis(PrivateClaims privateClaims, String accessToken);

  void processLogoutRedis(Long accountId);

  void processWithdrawal(Long accountId);
}
