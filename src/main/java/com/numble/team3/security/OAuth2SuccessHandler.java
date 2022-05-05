package com.numble.team3.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.jwt.PrivateClaims;
import com.numble.team3.jwt.TokenHelper;
import com.numble.team3.sign.application.response.TokenDto;
import com.numble.team3.sign.infra.SignRedisHelper;
import java.io.IOException;
import java.io.PrintWriter;
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
  private final SecurityUtils securityUtils;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    accountRepository.findByEmail((String) oAuth2User.getAttributes().get("email"))
      .ifPresentOrElse(account -> processAccount(account, response),
        () -> processAccount(accountRepository.save(convertAccount(oAuth2User)), response));
  }

  private Account convertAccount(OAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();

    return Account.createSignUpOauth2Account((String) attributes.get("email"),
      (String) attributes.get("nickname"), (String) attributes.get("profile"));
  }

  private void processAccount(Account account, HttpServletResponse response) {
    PrivateClaims privateClaims = createPrivateClaims(account);
    String accessToken = accessTokenHelper.createToken(privateClaims);
    String refreshToken = refreshTokenHelper.createToken(privateClaims);

    securityUtils.oauth2ProcessAccount(account.getId(), accessToken, refreshToken);

    createTokenCookie(response, refreshToken);
    try {
      writeTokenResponse(response, accessToken, refreshToken);
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  private void writeTokenResponse(HttpServletResponse response, String accessToken,
    String refreshToken) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    response.setContentType("application/json");

    PrintWriter writer = response.getWriter();
    writer.println(objectMapper.writeValueAsString(new TokenDto(accessToken, refreshToken)));
    writer.flush();
  }

  private void createTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie refreshTokenCookie = new Cookie("refreshToken",
      URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setMaxAge(604800);
    refreshTokenCookie.setPath("/");

    response.addCookie(refreshTokenCookie);
  }

  private PrivateClaims createPrivateClaims(Account account) {
    return new PrivateClaims(String.valueOf(account.getId()),
      List.of(account.getRoleType().toString()));
  }
}