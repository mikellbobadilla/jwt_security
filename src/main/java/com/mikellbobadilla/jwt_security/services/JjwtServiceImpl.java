package com.mikellbobadilla.jwt_security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;


import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/* Other implementation using other dependencies */
public class JjwtServiceImpl implements JwtService {

  @Value( "${jwt.secret}" )
  private String SECRET_KEY;

  private Key getSignKey(){
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public Claims getAllClaims(String token){
    return Jwts.parserBuilder()
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  @Override
  public String createToken(UserDetails userDetails) {
    return Jwts.builder()
      .setHeaderParam("typ", "JWT")
      .setSubject(userDetails.getUsername())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
      .signWith(getSignKey())
      .compact();
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = getSubject(token);
    final Date expiration = getExpiration(token);
    return (username.equals(userDetails.getUsername()) && !expiration.before(new Date()));
  }

  @Override
  public Date getExpiration(String token) {
    return getAllClaims(token).getExpiration();
  }

  @Override
  public String getSubject(String token) {
    return getAllClaims(token).getSubject();
  }
}
