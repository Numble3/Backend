package com.numble.team3.security;

import java.util.Optional;

public interface SecurityUtils {

  boolean validToken(String token, String key, Optional<String> id);

  void oauth2ProcessAccount(Long accountId, String accessToken, String refreshToken);
}
