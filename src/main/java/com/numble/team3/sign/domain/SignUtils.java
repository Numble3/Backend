package com.numble.team3.sign.domain;

import com.numble.team3.jwt.PrivateClaims;

public interface SignUtils {

  void processSignIn(Long accountId, String accessToken, String refreshToken);

  void validationRefreshToken(PrivateClaims privateClaims, String refreshToken);

  void changeAccessToken(PrivateClaims privateClaims, String accessToken);

  void deleteToken(Long accountId);

  void processWithdrawal(Long accountId);
}
