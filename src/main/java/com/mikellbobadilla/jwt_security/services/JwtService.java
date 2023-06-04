package com.mikellbobadilla.jwt_security.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
  public String createToken(UserDetails userDetails);
  public boolean isTokenValid(String token, UserDetails userDetails);
  public Date getExpiration(String token);
  public String getSubject(String token);
}
