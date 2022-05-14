package com.numble.team3.account.resolver;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.sign.TokenFailureException;
import com.numble.team3.jwt.PrivateClaims;
import com.numble.team3.jwt.TokenHelper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginMethodArgumentResolver implements HandlerMethodArgumentResolver {
  private final TokenHelper accessTokenHelper;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(LoginUser.class) != null
        && parameter.getParameterType().equals(UserInfo.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws RuntimeException {
    String authorizationHeader = webRequest.getHeader("Authorization");
    if (authorizationHeader == null) {
      return new UserInfo(null, null);
    }

    PrivateClaims privateClaims =
        accessTokenHelper.parse(authorizationHeader).orElseThrow(TokenFailureException::new);
    Long accountId = Long.valueOf(privateClaims.getAccountId());
    List<String> roleTypes = privateClaims.getRoleTypes();
    return new UserInfo(accountId, roleTypes);
  }
}
