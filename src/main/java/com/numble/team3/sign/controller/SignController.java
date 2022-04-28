package com.numble.team3.sign.controller;

import com.numble.team3.sign.application.SignService;
import com.numble.team3.sign.application.request.SignInDto;
import com.numble.team3.sign.application.request.SignUpDto;
import com.numble.team3.sign.application.response.TokenDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignController {

  private final SignService signService;

  @PostMapping("/sign-up")
  public ResponseEntity signUp(@Valid @RequestBody SignUpDto dto) {
    signService.signUp(dto);

    return new ResponseEntity(HttpStatus.CREATED);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInDto dto,
    HttpServletResponse response) {
    TokenDto token = signService.signIn(dto);
    createCookie(token.getRefreshToken(), 604800, response);

    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @GetMapping("/logout")
  public ResponseEntity logout(@RequestHeader(value = "Authorization") String accessToken,
    HttpServletResponse response) {
    signService.logout(accessToken);
    deleteCookie(response);

    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/refresh-token")
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

  private void createCookie(String token, int maxAge, HttpServletResponse response) {
    Cookie cookie = new Cookie("refreshToken", URLEncoder.encode(token, StandardCharsets.UTF_8));
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    cookie.setPath("/");

    response.addCookie(cookie);
  }

  private void deleteCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("refreshToken", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");

    response.addCookie(cookie);
  }
}
