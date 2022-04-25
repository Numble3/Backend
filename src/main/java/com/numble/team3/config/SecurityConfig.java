package com.numble.team3.config;

import com.numble.team3.jwt.TokenHelper;
import com.numble.team3.security.CustomAccessDeniedHandler;
import com.numble.team3.security.CustomAuthenticationEntryPoint;
import com.numble.team3.security.CustomOAuth2UserService;
import com.numble.team3.security.CustomUserDetailsService;
import com.numble.team3.security.JwtAuthenticationFilter;
import com.numble.team3.security.OAuth2SuccessHandler;
import com.numble.team3.sign.infra.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsService userDetailsService;
  private final RedisUtils redisUtils;
  private final TokenHelper accessTokenHelper;
  private final CustomOAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;

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
      .antMatchers("/", "/favicon.ico", "/profile").permitAll()
      .anyRequest().authenticated()

      .and()
      .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
      .and()
      .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
      .and()
      .addFilterBefore(
        new JwtAuthenticationFilter(userDetailsService, redisUtils, accessTokenHelper),
        UsernamePasswordAuthenticationFilter.class)

      .oauth2Login()
      .userInfoEndpoint().userService(oAuth2UserService)
      .and()
      .successHandler(oAuth2SuccessHandler).permitAll();
  }
}
