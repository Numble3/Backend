package com.numble.team3.account.application.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateMyAccountDto {

  private String profile;

  @NotBlank(message = "닉네임을 입력해주세요.")
  private String nickname;
}
