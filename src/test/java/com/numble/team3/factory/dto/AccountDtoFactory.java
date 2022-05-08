package com.numble.team3.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.numble.team3.account.application.request.UpdateMyAccountDto;
import com.numble.team3.account.application.response.GetMyAccountDto;
import com.numble.team3.admin.application.response.GetAccountDetailDto;
import com.numble.team3.admin.application.response.GetAccountListDto;
import com.numble.team3.admin.application.response.GetAccountSimpleDto;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public class AccountDtoFactory {

  public static GetAccountSimpleDto createGetAccountSimpleDto(Long id, String email, String nickname, String lastLogin) throws Exception {
    Constructor<GetAccountSimpleDto> constructor =
      GetAccountSimpleDto.class.getDeclaredConstructor(Long.class, String.class, String.class, String.class, LocalDateTime.class);
    constructor.setAccessible(true);

    return constructor.newInstance(id, email, nickname, lastLogin, LocalDateTime.now());
  }

  public static GetAccountListDto createGetAccountListDto(List<GetAccountSimpleDto> accounts, PageRequest pageRequest, int totalPage) throws Exception {
    Constructor<GetAccountListDto> constructor =
      GetAccountListDto.class.getDeclaredConstructor(List.class, Long.class, int.class, int.class, int.class);
    constructor.setAccessible(true);

    return constructor.newInstance(accounts, Long.valueOf(accounts.size()), pageRequest.getPageNumber() + 1, totalPage, pageRequest.getPageSize());
  }

  public static UpdateMyAccountDto createUpdateMyAccountDto(String profile, String nickname) {
    UpdateMyAccountDto dto = new UpdateMyAccountDto();
    setField(dto, "profile", profile);
    setField(dto, "nickname", nickname);

    return dto;
  }

  public static GetMyAccountDto createGetMyAccountDto(String email, String profile, String nickname) {
    return new GetMyAccountDto(email, profile, nickname);
  }

  public static GetAccountDetailDto createGetAccountDetailDto(Long id, String email, String nickname, String lastLogin) throws Exception {
    Constructor<GetAccountDetailDto> constructor =
      GetAccountDetailDto.class.getDeclaredConstructor(Long.class, String.class, String.class, String.class, LocalDateTime.class);
    constructor.setAccessible(true);

    return constructor.newInstance(id, email, nickname, lastLogin, LocalDateTime.now());
  }
}