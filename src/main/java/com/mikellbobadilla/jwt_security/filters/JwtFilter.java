package com.mikellbobadilla.jwt_security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikellbobadilla.jwt_security.DTO.ErrorResponseDTO;
import com.mikellbobadilla.jwt_security.exception.MapExceptionHandler;
import com.mikellbobadilla.jwt_security.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private static final Logger log = Logger.getLogger(JwtFilter.class.getName());

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
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException, JWTVerificationException {
  /* ----------------------------------------------------------------------------------------------------------- */

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String token;
    final String username;
    try{

      if(authHeader == null || !authHeader.startsWith("Bearer ")){
        filterChain.doFilter(request, response);
        return;
      }
    token = authHeader.substring(7);
      username = jwtService.getSubject(token);
      if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(jwtService.isTokenValid(token, userDetails)){
          var authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
          );
      /* WebAuthenticationDetailsSource is used to recompile more details for example [origin ip, browser or device used, etc.] */
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);
    }catch (RuntimeException exc){

      log.severe(exc.getMessage());

      ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
        HttpStatus.FORBIDDEN, "Your not authenticate or Your session has expired",
        new Date()
      );
      var obj = new ObjectMapper();
      response.setStatus(403);
      response.getWriter().write(obj.writeValueAsString(errorResponseDTO));
    }
  }

}
