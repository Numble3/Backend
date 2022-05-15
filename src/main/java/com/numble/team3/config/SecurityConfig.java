package com.numble.team3.config;

import com.numble.team3.jwt.TokenHelper;
import com.numble.team3.security.CustomAccessDeniedHandler;
import com.numble.team3.security.CustomAuthenticationEntryPoint;
import com.numble.team3.security.CustomOAuth2UserService;
import com.numble.team3.security.CustomUserDetailsService;
import com.numble.team3.security.JwtAuthenticationFilter;
import com.numble.team3.security.OAuth2SuccessHandler;
import com.numble.team3.security.SecurityUtils;
import com.numble.team3.sign.infra.SignRedisHelper;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsService userDetailsService;
  private final SecurityUtils securityUtils;
  private final TokenHelper accessTokenHelper;
  private final CustomOAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  private static final String[] SWAGGER_IGNORE_PATH = {
    "/v2/api-docs",
    "/swagger-resources",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/swagger-ui.html",
    "/v3/api-docs/**",
    "/swagger-ui/**"
  };

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
      .antMatchers("/h2-console/**", "/profile")
      .antMatchers(SWAGGER_IGNORE_PATH)
      .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .httpBasic().disable()
      .formLogin().disable()
      .csrf().disable()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and().cors()

      .and()
      .authorizeRequests()
      .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
      .antMatchers("/api/sign-in", "/api/sign-up", "/api/refresh-token").permitAll()
      .antMatchers("/api/likes/rank/day/**").permitAll()
      .antMatchers(HttpMethod.GET, "/api/videos/**").permitAll()

      .anyRequest().authenticated()

      .and()
      .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
      .and()
      .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
      .and()
      .addFilterBefore(
        new JwtAuthenticationFilter(userDetailsService, securityUtils, accessTokenHelper),
        UsernamePasswordAuthenticationFilter.class)

      .oauth2Login()
      .userInfoEndpoint().userService(oAuth2UserService)
      .and()
      .successHandler(oAuth2SuccessHandler).permitAll();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedMethods(List.of("GET","POST","PUT","PATCH","OPTIONS","DELETE"));
    configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://master.d2welxu3900atg.amplifyapp.com"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
