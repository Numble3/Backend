package com.numble.team3.sign.application.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenDto {

  @ApiModelProperty(value = "access token")
  private String accessToken;

  @ApiModelProperty(value = "refresh token")
  private String refreshToken;
}
