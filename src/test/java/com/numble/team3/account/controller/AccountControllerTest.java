package com.numble.team3.account.controller;

import static com.numble.team3.factory.dto.AccountDtoFactory.createGetMyAccountDto;
import static com.numble.team3.factory.dto.AccountDtoFactory.createUpdateMyAccountDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.account.application.AccountService;
import com.numble.team3.account.application.request.UpdateMyAccountDto;
import com.numble.team3.account.application.response.GetMyAccountDto;
import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.resolver.LoginMethodArgumentResolver;
import com.numble.team3.account.resolver.UserInfo;
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

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("AccountController 테스트")
class AccountControllerTest {

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
      .setCustomArgumentResolvers(loginMethodArgumentResolver)
      .alwaysDo(print())
      .build();
  }

  @Test
  void getMyAccount_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    GetMyAccountDto getMyAccountDto =
      createGetMyAccountDto("test@email.com", null, "nickname");

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    given(accountService.getAccountByUserInfo(any(UserInfo.class))).willReturn(getMyAccountDto);

    // when
    ResultActions result = mockMvc.perform(
      get("/api/accounts")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.email").value("test@email.com"))
      .andExpect(jsonPath("$.nickname").value("nickname"));
  }

  @Test
  void updateMyAccount_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    UpdateMyAccountDto dto =
      createUpdateMyAccountDto(null, "change nickname");

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    willDoNothing().given(accountService).updateAccount(any(UserInfo.class), any(UpdateMyAccountDto.class));

    // when
    ResultActions result = mockMvc.perform(
      post("/api/accounts/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto))
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isOk());
  }

  @Test
  void withdrawalAccount_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);

    willDoNothing().given(accountService).withdrawalAccount(anyLong());
    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/accounts/withdrawal"));

    // then
    result
      .andExpect(status().isOk());
  }
}