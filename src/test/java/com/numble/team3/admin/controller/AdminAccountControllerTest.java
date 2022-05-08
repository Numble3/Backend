package com.numble.team3.admin.controller;

import static com.numble.team3.factory.dto.AccountDtoFactory.createGetAccountDetailDto;
import static com.numble.team3.factory.dto.AccountDtoFactory.createGetAccountListDto;
import static com.numble.team3.factory.dto.AccountDtoFactory.createGetAccountSimpleDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.numble.team3.account.application.AccountService;
import com.numble.team3.admin.application.response.GetAccountDetailDto;
import com.numble.team3.admin.application.response.GetAccountListDto;
import com.numble.team3.admin.application.response.GetAccountSimpleDto;
import com.numble.team3.sign.application.SignService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("AdminAccountController 테스트")
class AdminAccountControllerTest {

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
      .alwaysDo(print())
      .build();
  }

  @Test
  void getAccounts_테스트() throws Exception {
    // given
    PageRequest pageRequest = PageRequest.of(0, 1);
    GetAccountSimpleDto getAccountSimpleDto =
      createGetAccountSimpleDto(1L, "test@email.com", "nickname", "2022-02-02");
    GetAccountListDto getAccountListDto =
      createGetAccountListDto(List.of(getAccountSimpleDto), pageRequest, 1);

    given(accountService.getAccounts(any(PageRequest.class))).willReturn(getAccountListDto);

    // when
    ResultActions result = mockMvc.perform(
      get("/api/admin//accounts/all")
        .param("page", "1")
        .param("size", "3")
        .accept(MediaType.APPLICATION_JSON_VALUE)
    );

    // then
    result
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.accountDtos").exists())
      .andExpect(jsonPath("$.accountDtos[0].id").value(1))
      .andExpect(jsonPath("$.accountDtos[0].email").value("test@email.com"))
      .andExpect(jsonPath("$.accountDtos[0].nickname").value("nickname"))
      .andExpect(jsonPath("$.accountDtos[0].lastLogin").value("2022-02-02"))
      .andExpect(jsonPath("$.accountDtos[0].createdAt").exists())
      .andExpect(jsonPath("$.totalCount").value(1))
      .andExpect(jsonPath("$.totalPage").value(1))
      .andExpect(jsonPath("$.size").value(1));
  }

  @Test
  void getAccount_테스트() throws Exception {
    // given
    GetAccountDetailDto getAccountDetailDto =
      createGetAccountDetailDto(1L, "test@email.com", "nickname", "2022-02-02");

    given(accountService.getAccountByAccountId(anyLong())).willReturn(getAccountDetailDto);

    // when
    ResultActions result = mockMvc.perform(
      get("/api/admin/accounts/{id}", 1)
        .accept(MediaType.APPLICATION_JSON_VALUE)
    );

    // then
    result
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(1))
      .andExpect(jsonPath("$.email").value("test@email.com"))
      .andExpect(jsonPath("$.nickname").value("nickname"))
      .andExpect(jsonPath("$.lastLogin").value("2022-02-02"))
      .andExpect(jsonPath("$.createdAt").exists());
  }

  @Test
  void withdrawalAccount_테스트() throws Exception {
    // given
    willDoNothing().given(accountService).withdrawalAccount(anyLong());

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/admin/accounts/withdrawal/{id}", 1)
    );

    // then
    result
      .andExpect(status().isOk());

    verify(accountService).withdrawalAccount(anyLong());
  }

  @Test
  void deleteAccessToken_테스트() throws Exception {
    // given
    willDoNothing().given(signService).deleteToken(anyLong());

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/admin/accounts/access-token/{id}", 1)
    );

    // then
    result
      .andExpect(status().isOk());

    verify(signService).deleteToken(anyLong());
  }
}