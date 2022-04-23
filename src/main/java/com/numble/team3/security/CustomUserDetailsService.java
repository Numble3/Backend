package com.numble.team3.security;

import com.numble.team3.jwt.PrivateClaims;
import com.numble.team3.jwt.TokenHelper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final TokenHelper accessTokenHelper;

  @Override
  public CustomUserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
    return accessTokenHelper.parse(token)
      .map(this::convert)
      .orElse(null);
  }

  private CustomUserDetails convert(PrivateClaims privateClaims) {
    return new CustomUserDetails(
      privateClaims.getAccountId(),
      privateClaims.getRoleTypes().stream().map(SimpleGrantedAuthority::new).collect(
        Collectors.toSet())
    );
  }
}
