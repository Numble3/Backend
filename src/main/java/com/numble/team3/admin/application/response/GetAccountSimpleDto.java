package com.numble.team3.admin.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.account.domain.Account;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAccountSimpleDto {

  private Long id;
  private String email;
  private String nickname;
  private String lastLogin;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  public void changeLastLogin(String lastLogin) {
    this.lastLogin = lastLogin;
  }

  public static GetAccountSimpleDto fromEntity(Account account) {
    return GetAccountSimpleDto.builder()
      .id(account.getId())
      .email(account.getEmail())
      .nickname(account.getNickname())
      .lastLogin(account.getLastLogin())
      .createdAt(account.getCreatedAt())
      .build();
  }
}

