package com.numble.team3.account.application.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateMyAccountDto {

  @ApiModelProperty(value = "profile url 경로", required = false)
  private String profile;

  @ApiModelProperty(value = "닉네임", required = true)
  @NotBlank(message = "닉네임을 입력해주세요.")
  private String nickname;
}
