package com.mikellbobadilla.jwt_security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtServiceImpl implements JwtService {

  @Value( "${jwt.secret}" )
  private String SECRET_KEY;

  private Algorithm getSignInKey(){
    return Algorithm.HMAC256(SECRET_KEY);
  }

  private DecodedJWT decodeToken(String token){
    return JWT.require(getSignInKey())
      .build()
      .verify(token);
  }

  @Override
  public String createToken(UserDetails userDetails){
    return JWT.create()
      .withIssuer(userDetails.getUsername())
      .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
      .sign(getSignInKey());
  }

  public Map<String, Claim> getAllClaims(String token){
    return JWT.decode(token).getClaims();
  }

  @Override
  public String getSubject(String token){
    return decodeToken(token).getIssuer();
  }

  @Override
  public Date getExpiration(String token){
    return decodeToken(token).getExpiresAt();
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails){
    final String username = getSubject(token);
    final Date expiration = getExpiration(token);

    return (username.equals(userDetails.getUsername()) && !expiration.before(new Date()));
  }
}
