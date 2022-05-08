package com.numble.team3.account.controller;

import static com.numble.team3.factory.dto.AccountDtoFactory.createUpdateMyAccountDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.account.application.AccountService;
import com.numble.team3.account.application.advice.AccountRestControllerAdvice;
import com.numble.team3.account.application.request.UpdateMyAccountDto;
import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.resolver.LoginMethodArgumentResolver;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.exception.account.AccountNicknameAlreadyExistsException;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.account.AccountWithdrawalException;
import com.numble.team3.factory.UserInfoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("AccountRestControllerAdvice 테스트")
public class AccountControllerAdviceTest {

  @Mock
  AccountService accountService;

  @Mock
  LoginMethodArgumentResolver loginMethodArgumentResolver;

  @InjectMocks
  AccountController accountController;

  MockMvc mockMvc;
  ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void beforeEach() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(accountController)
      .setControllerAdvice(new AccountRestControllerAdvice())
      .setCustomArgumentResolvers(loginMethodArgumentResolver)
      .addFilter(new CharacterEncodingFilter("UTF-8", true))
      .alwaysDo(print())
      .build();
  }

  @Test
  void getMyAccount_없는_회원_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    given(accountService.getAccountByUserInfo(any(UserInfo.class))).willThrow(new AccountNotFoundException());

    // when
    ResultActions result = mockMvc.perform(
      get("/api/accounts/")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
  }

  @Test
  void getMyAccount_탈퇴_회원_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    given(accountService.getAccountByUserInfo(any(UserInfo.class))).willThrow(new AccountWithdrawalException());

    // when
    ResultActions result = mockMvc.perform(
      get("/api/accounts/")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("이미 탈퇴처리 된 회원입니다."));
  }

  @Test
  void updateMyAccount_없는_회원_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    UpdateMyAccountDto dto = createUpdateMyAccountDto("profile", "nickname");

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    willThrow(new AccountNotFoundException()).given(accountService).updateAccount(any(UserInfo.class), any(UpdateMyAccountDto.class));

    // when
    ResultActions result = mockMvc.perform(
      post("/api/accounts/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto))
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
  }

  @Test
  void updateMyAccount_탈퇴_회원_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    UpdateMyAccountDto dto = createUpdateMyAccountDto("profile", "nickname");

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    willThrow(new AccountWithdrawalException()).given(accountService).updateAccount(any(UserInfo.class), any(UpdateMyAccountDto.class));

    // when
    ResultActions result = mockMvc.perform(
      post("/api/accounts/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto))
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("이미 탈퇴처리 된 회원입니다."));
  }

  @Test
  void updateMyAccount_nickname_누락_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    UpdateMyAccountDto dto = createUpdateMyAccountDto("profile", "nickname");

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    willThrow(new AccountNicknameAlreadyExistsException()).given(accountService).updateAccount(any(UserInfo.class), any(UpdateMyAccountDto.class));

    // when
    ResultActions result = mockMvc.perform(
      post("/api/accounts/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto))
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.message").value("이미 존재하는 닉네임입니다."));
  }

  @Test
  void withdrawalAccount_없는_회원_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    willThrow(new AccountNotFoundException()).given(accountService).withdrawalAccount(anyLong());

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/accounts/withdrawal")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
  }
}
