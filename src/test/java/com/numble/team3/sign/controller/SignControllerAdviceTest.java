package com.numble.team3.sign.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.account.AccountWithdrawalException;
import com.numble.team3.exception.sign.SignInFailureException;
import com.numble.team3.exception.sign.TokenFailureException;
import com.numble.team3.factory.dto.SignDtoFactory;
import com.numble.team3.sign.application.SignService;
import com.numble.team3.sign.application.advice.SignRestControllerAdvice;
import com.numble.team3.sign.application.request.SignInDto;
import com.numble.team3.sign.application.request.SignUpDto;
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
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("SignRestControllerAdvice 테스트")
public class SignControllerAdviceTest {

  @Mock
  SignService signService;

  @InjectMocks
  SignController signController;

  ObjectMapper objectMapper = new ObjectMapper();
  MockMvc mockMvc;

  @BeforeEach
  void beforeEach() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(signController)
      .setControllerAdvice(new SignRestControllerAdvice())
      .addFilter(new CharacterEncodingFilter("UTF-8", true))
      .alwaysDo(print())
      .build();
  }

  @Test
  void signUp_email_누락_실패_테스트() throws Exception {
    // given
    SignUpDto dto = SignDtoFactory.createSignUpDto(null, "1234", "nickname");

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-up")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("이메일을 입력해주세요."));
  }

  @Test
  void signUp_email_형식_실패_테스트() throws Exception {
    // given
    SignUpDto dto = SignDtoFactory.createSignUpDto("이메일 형식 아님", "1234", "nickname");

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-up")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("이메일 형식으로 입력해주세요."));
  }

  @Test
  void signUp_nickname_누락_실패_테스트() throws Exception {
    // given
    SignUpDto dto = SignDtoFactory.createSignUpDto("test@email.com", "1234", null);

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-up")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("닉네임을 입력해주세요."));
  }

  @Test
  void signUp_password_누락_실패_테스트() throws Exception {
    // given
    SignUpDto dto = SignDtoFactory.createSignUpDto("test@email.com", null, "닉네임");

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-up")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("비밀번호를 입력해주세요."));
  }

  @Test
  void signIn_email_누락_실패_테스트() throws Exception {
    // given
    SignInDto dto = SignDtoFactory.createSignInDto(null, "1234");

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-in")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("이메일을 입력해주세요."));
  }

  @Test
  void signIn_password_누락_실패_테스트() throws Exception {
    // given
    SignInDto dto = SignDtoFactory.createSignInDto("test@email.com", null);

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-in")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("비밀번호를 입력해주세요."));
  }

  @Test
  void signIn_없는_계정_실패_테스트() throws Exception {
    // given
    SignInDto dto = SignDtoFactory.createSignInDto("test@email.com", "1234");

    given(signService.signIn(any(SignInDto.class))).willThrow(new SignInFailureException());

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-in")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)));

    // then
    result
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.message").value("로그인에 실패했습니다."));
  }

  @Test
  void signIn_탈퇴한_계정_실패_테스트() throws Exception {
    // given
    SignInDto dto = SignDtoFactory.createSignInDto("test@email.com", "1234");

    given(signService.signIn(any(SignInDto.class))).willThrow(new AccountWithdrawalException());

    // when
    ResultActions result = mockMvc.perform(
      post("/api/sign-in")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("이미 탈퇴처리 된 계정입니다."));
  }

  @Test
  void logout_헤더_토큰_누락_실패_테스트() throws Exception {
    // given

    // when
    ResultActions result = mockMvc.perform(
      get("/api/logout"));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Authorization 헤더가 누락되었습니다."));
  }

  @Test
  void logout_토큰_만료_실패_이벤트() throws Exception {
    // given
    willThrow(new TokenFailureException()).given(signService).logout(anyString());

    // when
    ResultActions result = mockMvc.perform(
      get("/api/logout").header("Authorization", "Bearer accessToken"));

    // then
    result
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
  }

  @Test
  void createAccessTokenByRefreshToken_쿠키_누락_실패_테스트() throws Exception {
    // given

    // when
    ResultActions result = mockMvc.perform(
      get("/api/refresh-token"));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("refreshToken 쿠키가 누락되었습니다."));
  }

  @Test
  void createAccessTokenByRefreshToken_쿠키_이름_실패_테스트() throws Exception {
    // given
    Cookie mockCookie = Mockito.mock(Cookie.class);
    given(mockCookie.getName()).willReturn("refreshToken 쿠키 아님");

    // when
    ResultActions result = mockMvc.perform(
      get("/api/refresh-token").cookie(mockCookie));

    // then
    result
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.message").value("refreshToken 쿠키가 누락되었습니다."));
  }

  @Test
  void createAccessTokenByRefreshToken_토큰_만료_실패_테스트() throws Exception {
    // given
    Cookie mockCookie = Mockito.mock(Cookie.class);
    given(mockCookie.getName()).willReturn("refreshToken");
    given(mockCookie.getValue()).willReturn(URLEncoder.encode("refreshToken", StandardCharsets.UTF_8));
    given(signService.createAccessTokenByRefreshToken(anyString())).willThrow(new TokenFailureException());

    // when
    ResultActions result = mockMvc.perform(
      get("/api/refresh-token").cookie(mockCookie));

    // then
    result
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
  }

  @Test
  void accountWithdrawal_헤더_토큰_누락_실패_테스트() throws Exception {
    // given

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/withdrawal"));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Authorization 헤더가 누락되었습니다."));
  }

  @Test
  void accountWithdrawal_토큰_만료_실패_테스트() throws Exception {
    // given
    willThrow(new TokenFailureException()).given(signService).withdrawal(anyString());

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/withdrawal")
        .header("Authorization", "Bearer accessToken"));

    // then
    result
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
  }

  @Test
  void accountWithdrawal_없는_계정_실패_테스트() throws Exception {
    // given
    willThrow(new AccountNotFoundException()).given(signService).withdrawal(anyString());

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/withdrawal").header("Authorization", "Bearer accessToken"));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("존재하지 않는 이메일입니다."));
  }
}

