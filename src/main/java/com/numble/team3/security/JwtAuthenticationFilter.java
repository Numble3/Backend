package com.numble.team3.security;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

  private final CustomUserDetailsService userDetailsService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
      extractToken(request).map(userDetailsService::loadUserByUsername).ifPresent(this::setAuthentication);
      chain.doFilter(request, response);
  }

  private void setAuthentication(CustomUserDetails userDetails) {
    SecurityContextHolder.getContext()
      .setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
  }

  private Optional<String> extractToken(ServletRequest request) {
    return Optional.ofNullable(((HttpServletRequest) request).getHeader("Authorization"));
  }
}
