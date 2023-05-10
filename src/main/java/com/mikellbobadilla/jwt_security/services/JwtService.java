package com.mikellbobadilla.jwt_security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  private Key getSignInKey(){
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(UserDetails userDetails){

    return Jwts.builder()
      .setHeaderParam("typ", "JWT")
      .setSubject(userDetails.getUsername())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
      .signWith(getSignInKey())
      .compact();
  }

  public Claims getAllClaims(String token){

    return Jwts.parserBuilder()
      .setSigningKey(getSignInKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public String getSubject(String token){
    return getAllClaims(token).getSubject();
  }

  public Date getExpiration(String token){
    return getAllClaims(token).getExpiration();
  }

  public boolean isTokenValid(String token, UserDetails userDetails){
    final String username = getSubject(token);
    final Date expiration = getExpiration(token);

    return (username.equals(userDetails.getUsername()) && !expiration.before(new Date()));
  }
}
