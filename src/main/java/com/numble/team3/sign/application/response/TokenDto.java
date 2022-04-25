package com.numble.team3.sign.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenDto {

  private String accessToken;
  private String refreshToken;
}
