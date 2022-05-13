package com.numble.team3.sign.controller;

import com.numble.team3.sign.annotation.AccountWithdrawalSwagger;
import com.numble.team3.sign.annotation.CreateAccessTokenSwagger;
import com.numble.team3.sign.annotation.LogoutSwagger;
import com.numble.team3.sign.annotation.SignInSwagger;
import com.numble.team3.sign.annotation.SignUpSwagger;
import com.numble.team3.sign.application.SignService;
import com.numble.team3.sign.application.request.SignInDto;
import com.numble.team3.sign.application.request.SignUpDto;
import com.numble.team3.sign.application.response.TokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"회원 가입, 로그인, 로그아웃, access token 재발급, 회원 탈퇴"})
public class SignController {

  private final SignService signService;

  @SignUpSwagger
  @PostMapping(value = "/sign-up", produces = "application/json", consumes = "application/json")
  public ResponseEntity signUp(@Valid @RequestBody SignUpDto dto) {
    signService.signUp(dto);

    return new ResponseEntity(HttpStatus.CREATED);
  }

  @SignInSwagger
  @PostMapping(value = "/sign-in", produces = "application/json", consumes = "application/json")
  public ResponseEntity<TokenDto> signIn(
    @Valid @RequestBody SignInDto dto,
    HttpServletResponse response) {
    TokenDto token = signService.signIn(dto);
    createCookie(token.getRefreshToken(), 604800, response);

    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @LogoutSwagger
  @GetMapping(value = "/logout", produces = "application/json")
  public ResponseEntity logout(
    @ApiParam(value = "access token", required = true) @RequestHeader(value = "Authorization") String accessToken,
    HttpServletResponse response) {
    signService.logout(accessToken);
    deleteCookie(response);

    return new ResponseEntity(HttpStatus.OK);
  }

  @CreateAccessTokenSwagger
  @GetMapping(value = "/refresh-token", produces = "application/json")
  public ResponseEntity createAccessTokenByRefreshToken(HttpServletRequest request) {
    for (Cookie cookie : request.getCookies()) {
      if (cookie.getName().equals("refreshToken")) {
        return new ResponseEntity(signService.createAccessTokenByRefreshToken(
          URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8)), HttpStatus.OK);
      }
    }
    Map<String, String> message = new HashMap<>();
    message.put("message", "refreshToken 쿠키가 누락되었습니다.");
    return new ResponseEntity(message, HttpStatus.UNAUTHORIZED);
  }

  @AccountWithdrawalSwagger
  @DeleteMapping(value = "/withdrawal", produces = "application/json")
  public ResponseEntity accountWithdrawal(
    @ApiParam(value = "access token", required = true) @RequestHeader(value = "Authorization") String accessToken,
    HttpServletResponse response) {
    signService.withdrawal(accessToken);
    deleteCookie(response);

    return new ResponseEntity(HttpStatus.OK);
  }

  private void createCookie(String token, int maxAge, HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie.from("refreshToken", URLEncoder.encode(token, StandardCharsets.UTF_8))
      .secure(true)
      .httpOnly(true)
      .path("/")
      .maxAge(maxAge)
      .sameSite("None")
      .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  private void deleteCookie(HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
      .secure(true)
      .httpOnly(true)
      .path("/")
      .maxAge(0)
      .sameSite("None")
      .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }
}
