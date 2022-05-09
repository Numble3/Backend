package com.numble.team3.sign.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.exception.account.AccountEmailAlreadyExistsException;
import com.numble.team3.exception.account.AccountNicknameAlreadyExistsException;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.account.AccountWithdrawalException;
import com.numble.team3.exception.sign.SignInFailureException;
import com.numble.team3.exception.sign.TokenFailureException;
import com.numble.team3.factory.dto.SignDtoFactory;
import com.numble.team3.factory.entity.AccountEntityFactory;
import com.numble.team3.jwt.PrivateClaims;
import com.numble.team3.jwt.TokenHelper;
import com.numble.team3.sign.application.request.SignInDto;
import com.numble.team3.sign.application.request.SignUpDto;
import com.numble.team3.sign.application.response.TokenDto;
import com.numble.team3.sign.domain.SignUtils;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("SignService 테스트")
class SignServiceTest {

  @Mock
  JpaAccountRepository accountRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  TokenHelper accessTokenHelper;

  @Mock
  TokenHelper refreshTokenHelper;

  @Mock
  SignUtils signUtils;

  SignService signService;

  @BeforeEach
  void beforeEach() {
    signService =
      new SignService(accountRepository, passwordEncoder, accessTokenHelper, refreshTokenHelper, signUtils);
  }

  @Test
  void signUp_성공_테스트() throws Exception {
    // given
    SignUpDto dto =
      SignDtoFactory.createSignUpDto("test@email.com", "1234", "닉네임");

    // when
    signService.signUp(dto);

    // then
    verify(accountRepository).save(any(Account.class));
  }

  @Test
  void signUp_이메일_중복_실패_테스트() throws Exception {
    // given
    SignUpDto dto =
      SignDtoFactory.createSignUpDto("중복 이메일@email.com", "1234", "닉네임");

    given(accountRepository.existsByEmail(anyString())).willReturn(true);

    // when, then
    assertThrows(AccountEmailAlreadyExistsException.class, () -> signService.signUp(dto));
  }

  @Test
  void signUp_닉네임_중복_실패_테스트() throws Exception {
    // given
    SignUpDto dto =
      SignDtoFactory.createSignUpDto("test@email.com", "1234", "중복 닉네임");

    given(accountRepository.existsByEmail(anyString())).willReturn(false);
    given(accountRepository.existsByNickname(anyString())).willReturn(true);

    // when, then
    assertThrows(AccountNicknameAlreadyExistsException.class, () -> signService.signUp(dto));
  }

  @Test
  void signIn_성공_테스트() throws Exception {
    // given
    Account account =
      AccountEntityFactory.createAccount(
        1L, "test@email.com", "1234", "닉네임", RoleType.ROLE_USER);
    SignInDto dto = SignDtoFactory.createSignInDto("test@email.com", "1234");

    given(accountRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(account));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
    given(accessTokenHelper.createToken(any())).willReturn("accessToken");
    given(refreshTokenHelper.createToken(any())).willReturn("refreshToken");

    // when
    TokenDto token = signService.signIn(dto);

    // then
    assertEquals("accessToken", token.getAccessToken());
    assertEquals("refreshToken", token.getRefreshToken());

    verify(signUtils).processSignIn(anyLong(), anyString(), anyString());
  }

  @Test
  void signIn_없는_이메일_실패_테스트() throws Exception {
    // given
    SignInDto dto = SignDtoFactory.createSignInDto("없는 이메일@email.com", "1234");

    given(accountRepository.findByEmail(anyString())).willReturn(Optional.empty());

    // when, then
    assertThrows(SignInFailureException.class, () -> signService.signIn(dto));
  }

  @Test
  void signIn_일치하지_않는_비밀번호_실패_테스트() throws Exception {
    // given
    Account account =
      AccountEntityFactory.createAccount(
        1L, "test@email.com", "1234", "닉네임", RoleType.ROLE_USER);
    SignInDto dto = SignDtoFactory.createSignInDto("없는 이메일@email.com", "다른 비밀번호");

    given(accountRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(account));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

    // when, then
    assertThrows(SignInFailureException.class, () -> signService.signIn(dto));
  }

  @Test
  void signIn_탈퇴한_회원_실패_테스트() throws Exception {
    // given
    Account account =
      AccountEntityFactory.createDeletedAccount(
        1L, "test@email.com", "1234", "닉네임", RoleType.ROLE_USER);
    SignInDto dto = SignDtoFactory.createSignInDto("test@email.com", "1234");

    given(accountRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(account));

    // when, then
    assertThrows(AccountWithdrawalException.class, () -> signService.signIn(dto));
  }

  @Test
  void createAccessTokenByRefreshToken_성공_테스트() {
    // given
    String refreshToken = "refreshToken";
    PrivateClaims privateClaims =
      new PrivateClaims(String.valueOf(1L), List.of(RoleType.ROLE_USER.toString()));

    given(refreshTokenHelper.parse(anyString())).willReturn(Optional.ofNullable(privateClaims));
    given(accessTokenHelper.createToken(any())).willReturn("new accessToken");

    // when
    TokenDto token = signService.createAccessTokenByRefreshToken(refreshToken);

    // then
    assertEquals("new accessToken", token.getAccessToken());

    verify(signUtils).changeAccessToken(any(PrivateClaims.class), anyString());
  }

  @Test
  void createAccessTokenByRefreshToken_refreshToken_만료_실패_테스트() {
    // given
    given(refreshTokenHelper.parse(anyString())).willReturn(Optional.empty());

    // when, then
    assertThrows(TokenFailureException.class, () -> signService.createAccessTokenByRefreshToken("만료된 토큰"));
  }

  @Test
  void logout_성공_테스트() {
    // given
    String accessToken = "accessToken";
    PrivateClaims privateClaims =
      new PrivateClaims(String.valueOf(1L), List.of(RoleType.ROLE_USER.toString()));

    given(accessTokenHelper.parse(anyString())).willReturn(Optional.ofNullable(privateClaims));

    // when
    signService.logout(accessToken);

    // then
    signUtils.deleteToken(anyLong());
  }

  @Test
  void logout_만료된_토큰_실패_테스트() {
    // given
    given(accessTokenHelper.parse(anyString())).willReturn(Optional.empty());

    // when, then
    assertThrows(TokenFailureException.class, () -> signService.logout("만료된 토큰"));
  }

  @Test
  void withdrawal_성공_테스트() throws Exception {
    // given
    String accessToken = "accessToken";
    PrivateClaims privateClaims =
      new PrivateClaims(String.valueOf(1L), List.of(RoleType.ROLE_USER.toString()));
    Account account =
      AccountEntityFactory.createAccount(
        1L, "test@email.com", "1234", "닉네임", RoleType.ROLE_USER);

    given(accessTokenHelper.parse(anyString())).willReturn(Optional.ofNullable(privateClaims));
    given(accountRepository.findById(anyLong())).willReturn(Optional.ofNullable(account));

    // when
    signService.withdrawal(accessToken);

    // then
    assertEquals(true, account.isDeleted());

    verify(signUtils).processWithdrawal(anyLong());
  }

  @Test
  void withdrawal_토큰_만료_실패_테스트() {
    // given
    given(accessTokenHelper.parse(anyString())).willReturn(Optional.empty());

    // when, then
    assertThrows(TokenFailureException.class, () -> signService.withdrawal("만료된 토큰"));
  }

  @Test
  void withdrawal_없는_이메일_실패_테스트() throws Exception {
    // given
    String accessToken = "accessToken";
    PrivateClaims privateClaims =
      new PrivateClaims(String.valueOf(1L), List.of(RoleType.ROLE_USER.toString()));
    SignInDto dto = SignDtoFactory.createSignInDto("없는 이메일@email.com", "1234");

    given(accessTokenHelper.parse(anyString())).willReturn(Optional.ofNullable(privateClaims));
    given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

    // when, then
    assertThrows(AccountNotFoundException.class, () -> signService.withdrawal(accessToken));
  }
}
