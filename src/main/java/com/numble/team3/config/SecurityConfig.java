package com.numble.team3.config;

import com.numble.team3.security.CustomAccessDeniedHandler;
import com.numble.team3.security.CustomAuthenticationEntryPoint;
import com.numble.team3.security.CustomUserDetailsService;
import com.numble.team3.security.JwtAuthenticationFilter;
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
        new JwtAuthenticationFilter(userDetailsService),
        UsernamePasswordAuthenticationFilter.class);
  }
}
