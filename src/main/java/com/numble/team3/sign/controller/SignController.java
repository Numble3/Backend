package com.numble.team3.sign.controller;

import com.numble.team3.sign.application.SignService;
import com.numble.team3.sign.application.request.SignInDto;
import com.numble.team3.sign.application.request.SignUpDto;
import com.numble.team3.sign.application.response.TokenDto;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignController {

  private final SignService signService;

  @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity signUp(@Valid @RequestBody SignUpDto dto) {
    signService.signUp(dto);

    return new ResponseEntity(HttpStatus.CREATED);
  }

  @PostMapping(value = "/sign-in")
  public ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInDto dto,
    HttpServletResponse response) {
      TokenDto token = signService.signIn(dto);
      createCookie("accessToken", token.getAccessToken(), 1800, response);
      createCookie("refreshToken", token.getRefreshToken(), 604800, response);

      return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @GetMapping("/logout")
  public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
    for (Cookie cookie : request.getCookies()) {
      if (cookie.getName().equals("accessToken")) {
        signService.logout(cookie.getValue());
      }
    }

    deleteCookie("accessToken", response);
    deleteCookie("refreshToken", response);

    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/refresh-token")
  public ResponseEntity createAccessTokenByRefreshToken(HttpServletRequest request,
    HttpServletResponse response) {
    for (Cookie cookie : request.getCookies()) {
      if (cookie.getName().equals("refreshToken")) {
        TokenDto token = signService.createAccessTokenByRefreshToken(
          URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8));

        createCookie("accessToken", token.getAccessToken(), 1800, response);

        return new ResponseEntity(HttpStatus.OK);
      }
    }
    return new ResponseEntity(HttpStatus.UNAUTHORIZED);
  }

  private void createCookie(String key, String token, int maxAge, HttpServletResponse response) {
    Cookie cookie = new Cookie(key, URLEncoder.encode(token, StandardCharsets.UTF_8));
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    cookie.setPath("/");

    response.addCookie(cookie);
  }

  private void deleteCookie(String key, HttpServletResponse response) {
    Cookie cookie = new Cookie(key, null);
    cookie.setMaxAge(0);
    cookie.setPath("/");

    response.addCookie(cookie);
  }
}
