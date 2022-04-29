package com.numble.team3.config;

import com.numble.team3.jwt.TokenHelper;
import com.numble.team3.security.CustomAccessDeniedHandler;
import com.numble.team3.security.CustomAuthenticationEntryPoint;
import com.numble.team3.security.CustomOAuth2UserService;
import com.numble.team3.security.CustomUserDetailsService;
import com.numble.team3.security.JwtAuthenticationFilter;
import com.numble.team3.security.OAuth2SuccessHandler;
import com.numble.team3.sign.infra.SignRedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsService userDetailsService;
  private final SignRedisUtils signRedisUtils;
  private final TokenHelper accessTokenHelper;
  private final CustomOAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
      .antMatchers("/h2-console/**", "/favicon.ico", "/profile")
      .antMatchers("/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .httpBasic().disable()
      .formLogin().disable()
      .csrf().disable()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

      .and()
      .authorizeRequests()
      .antMatchers("/api/sign-in", "/api/sign-up").permitAll()
      .anyRequest().authenticated()

      .and()
      .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
      .and()
      .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
      .and()
      .addFilterBefore(
        new JwtAuthenticationFilter(userDetailsService, signRedisUtils, accessTokenHelper),
        UsernamePasswordAuthenticationFilter.class)

      .oauth2Login()
      .userInfoEndpoint().userService(oAuth2UserService)
      .and()
      .successHandler(oAuth2SuccessHandler).permitAll();
  }
}
