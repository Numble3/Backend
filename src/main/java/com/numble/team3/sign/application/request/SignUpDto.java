package com.numble.team3.sign.application.request;

import com.numble.team3.account.domain.Account;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpDto {

  @ApiModelProperty(value = "이메일 (중복 불가능)", required = true)
  @NotBlank(message = "이메일을 입력해주세요.")
  @Email(message = "이메일 형식으로 입력해주세요.")
  private String email;

  @ApiModelProperty(value = "비밀번호", required = true)
  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  @ApiModelProperty(value = "닉네임 (중복 불가능)", required = true)
  @NotBlank(message = "닉네임을 입력해주세요.")
  private String nickname;

  public static Account toEntity(SignUpDto dto, PasswordEncoder passwordEncoder) {
    return Account.createSignUpAccount(
      dto.getEmail(), dto.getNickname(), passwordEncoder.encode(dto.getPassword()));
  }
}
