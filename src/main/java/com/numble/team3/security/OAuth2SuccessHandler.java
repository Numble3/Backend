package com.numble.team3.security;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.jwt.PrivateClaims;
import com.numble.team3.jwt.TokenHelper;
import com.numble.team3.sign.application.response.TokenDto;
import com.numble.team3.sign.infra.RedisUtils;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final JpaAccountRepository accountRepository;
  private final TokenHelper accessTokenHelper;
  private final TokenHelper refreshTokenHelper;
  private final RedisUtils redisUtils;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    accountRepository.findByEmail((String) oAuth2User.getAttributes().get("email"))
      .ifPresentOrElse(account -> accountProcess(account, response),
        () -> accountProcess(accountRepository.save(convertAccount(oAuth2User)), response));
  }

  private Account convertAccount(OAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();

    return Account.createSignUpOauth2Account(
      (String) attributes.get("email"), (String) attributes.get("nickname"), (String) attributes.get("profile"));
  }

  private void accountProcess(Account account, HttpServletResponse response) {
    PrivateClaims privateClaims = createPrivateClaims(account);
    String accessToken = accessTokenHelper.createToken(privateClaims);
    String refreshToken = refreshTokenHelper.createToken(privateClaims);

    redisUtils.saveAccessToken(account.getId(), accessToken);
    redisUtils.saveRefreshToken(account.getId(), refreshToken);

    createTokenCookie(response, new TokenDto(accessToken, refreshToken));
  }

  private void createTokenCookie(HttpServletResponse response, TokenDto token) {
    Cookie accessTokenCookie = new Cookie("accessToken",
      URLEncoder.encode(token.getAccessToken(), StandardCharsets.UTF_8));
    accessTokenCookie.setSecure(true);
    accessTokenCookie.setHttpOnly(true);
    accessTokenCookie.setMaxAge(1800);
    accessTokenCookie.setPath("/");

    Cookie refreshTokenCookie = new Cookie("refreshToken",
      URLEncoder.encode(token.getRefreshToken(), StandardCharsets.UTF_8));
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setMaxAge(604800);
    refreshTokenCookie.setPath("/");

    response.addCookie(accessTokenCookie);
    response.addCookie(refreshTokenCookie);
  }

  private PrivateClaims createPrivateClaims(Account account) {
    return new PrivateClaims(String.valueOf(account.getId()),
      List.of(account.getRoleType().toString()));
  }
}