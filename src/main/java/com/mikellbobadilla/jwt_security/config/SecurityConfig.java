package com.mikellbobadilla.jwt_security.config;

import com.mikellbobadilla.jwt_security.filters.JwtFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthenticationProvider authProvider;
  private final JwtFilter jwtFilter;

  public SecurityConfig(AuthenticationProvider authProvider, JwtFilter jwtFilter) {
    this.authProvider = authProvider;
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

    http
      .csrf()
      .disable()
      .authorizeHttpRequests()
      /* url free to access to anybody */
      .requestMatchers("/api/auth/**")
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      /* Auth provider was implemented on AppConfig class */
      .authenticationProvider(authProvider)
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
