package com.mikellbobadilla.jwt_security.filters;

import com.mikellbobadilla.jwt_security.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain) throws ServletException, IOException {
  /* ----------------------------------------------------------------------------------------------------------- */

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String token;
    final String username;

    if(authHeader == null || !authHeader.startsWith("Bearer ")){
      filterChain.doFilter(request, response);
      return;
    }

    token = authHeader.substring(7);
    username = jwtService.getSubject(token);

    if(username == null && SecurityContextHolder.getContext().getAuthentication() == null){

      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      if(jwtService.isTokenValid(token, userDetails)){
        var authToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
    /* WebAuthenticationDetailsSource is used to recompile more details for example [origin ip, browser or device used, etc.] */
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        System.out.println(authToken.getDetails());
        System.out.println(authToken);
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
