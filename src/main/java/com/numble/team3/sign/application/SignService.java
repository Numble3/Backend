package com.numble.team3.sign.application;

import com.numble.team3.sign.application.request.SignInDto;
import com.numble.team3.sign.application.request.SignUpDto;
import com.numble.team3.sign.application.response.TokenDto;

public interface SignService {

  void signUp(SignUpDto dto);

  TokenDto signIn(SignInDto dto);

  TokenDto createAccessTokenByRefreshToken(String refreshToken);

  void logout(String accessToken);
}
