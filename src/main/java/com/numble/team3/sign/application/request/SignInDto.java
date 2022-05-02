package com.numble.team3.sign.application.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInDto {

  @ApiModelProperty(value = "이메일", required = true)
  @NotBlank(message = "이메일을 입력해주세요.")
  @Email(message = "이메일 형식으로 입력해주세요.")
  private String email;

  @ApiModelProperty(value = "비밀번호", required = true)
  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;
}
