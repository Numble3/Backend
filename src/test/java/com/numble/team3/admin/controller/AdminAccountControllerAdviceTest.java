package com.numble.team3.admin.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.numble.team3.account.application.AccountService;
import com.numble.team3.admin.application.advice.AdminAccountRestControllerAdvice;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.sign.application.SignService;
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
@DisplayName("AdminAccountRestControllerAdvice 테스트")
public class AdminAccountControllerAdviceTest {

  @Mock
  AccountService accountService;

  @Mock
  SignService signService;

  @InjectMocks
  AdminAccountController adminAccountController;

  MockMvc mockMvc;

  @BeforeEach
  void beforeEach() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(adminAccountController)
      .setControllerAdvice(new AdminAccountRestControllerAdvice())
      .alwaysDo(print())
      .build();
  }

  @Test
  void getAccount_없는_회원_실패_테스트() throws Exception {
    // given
    given(accountService.getAccountByAccountId(anyLong())).willThrow(new AccountNotFoundException());

    // when
    ResultActions result = mockMvc.perform(
      get("/api/admin/accounts/{id}", 1)
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
  }

  @Test
  void withdrawalAccount_없는_회원_실패_테스트() throws Exception {
    // given
    willThrow(new AccountNotFoundException()).given(accountService).withdrawalAccount(anyLong());

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/admin/accounts/withdrawal/{id}", 1)
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
  }
}