package com.numble.team3.account.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyAccountDto {

  private String email;
  private String profile;
  private String nickname;
}
