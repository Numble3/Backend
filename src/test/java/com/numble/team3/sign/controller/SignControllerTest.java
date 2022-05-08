package com.numble.team3.sign.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.factory.dto.SignDtoFactory;
import com.numble.team3.sign.application.SignService;
import com.numble.team3.sign.application.request.SignInDto;
import com.numble.team3.sign.application.request.SignUpDto;
import com.numble.team3.sign.application.response.TokenDto;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("SignController 테스트")
class SignControllerTest {

  @Mock
  SignService signService;

  @InjectMocks
  SignController signController;

  MockMvc mockMvc;
  ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void beforeEach() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(signController)
      .alwaysDo(print())
      .build();
  }

  @Test
  void signUp_테스트() throws Exception {
    // given
    SignUpDto dto = SignDtoFactory.createSignUpDto("test@email.com", "1234", "닉네임");

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-up")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto))
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result.andExpect(status().isCreated());
  }

  @Test
  void signIn_테스트() throws Exception {
    // given
    SignInDto dto = SignDtoFactory.createSignInDto("test@email.com", "1234");

    given(signService.signIn(any(SignInDto.class)))
      .willReturn(new TokenDto("accessToken", "refreshToken"));

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-in")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto))
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.accessToken").value("accessToken"))
      .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
      .andExpect(cookie().value("refreshToken", "refreshToken"))
      .andExpect(cookie().maxAge("refreshToken", 604800))
      .andExpect(cookie().path("refreshToken", "/"))
      .andExpect(cookie().secure("refreshToken", true))
      .andExpect(cookie().httpOnly("refreshToken", true));
  }

  @Test
  void logout_테스트() throws Exception {
    // given
    willDoNothing().given(signService).logout(anyString());

    // when
    ResultActions result = mockMvc.perform(
      get("/api/logout")
        .header("Authorization", "Bearer accessToken"));

    // then
    result
      .andExpect(status().isOk())
      .andExpect(cookie().path("refreshToken", "/"))
      .andExpect(cookie().maxAge("refreshToken", 0));
  }

  @Test
  void createAccessTokenByRefreshToken_테스트() throws Exception {
    // given
    Cookie mockCookie = Mockito.mock(Cookie.class);
    TokenDto token = new TokenDto("new-accessToken", "refreshToken");
    given(mockCookie.getName()).willReturn("refreshToken");
    given(mockCookie.getValue()).willReturn(URLEncoder.encode("refreshToken", StandardCharsets.UTF_8));
    given(signService.createAccessTokenByRefreshToken(anyString())).willReturn(token);

    // when
    ResultActions result = mockMvc.perform(get("/api/refresh-token").cookie(mockCookie));

    // then
    result
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.accessToken").value("new-accessToken"))
      .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
  }

  @Test
  void accountWithdrawal_테스트() throws Exception {
    // given
    willDoNothing().given(signService).withdrawal(anyString());

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/withdrawal")
        .header("Authorization", "Bearer accessToken"));

    // then
    result
      .andExpect(status().isOk())
      .andExpect(cookie().path("refreshToken", "/"))
      .andExpect(cookie().maxAge("refreshToken", 0));
  }
}