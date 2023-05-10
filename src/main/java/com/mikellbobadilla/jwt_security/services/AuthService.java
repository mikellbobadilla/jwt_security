package com.mikellbobadilla.jwt_security.services;

import com.mikellbobadilla.jwt_security.DTO.AuthDTO;
import com.mikellbobadilla.jwt_security.DTO.RegisterDTO;
import com.mikellbobadilla.jwt_security.entities.User;
import com.mikellbobadilla.jwt_security.entities.UserRole;
import com.mikellbobadilla.jwt_security.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final AuthenticationManager authManager;
  private final PasswordEncoder passwordEncoder;

  public AuthService(
    JwtService jwtService,
    UserRepository userRepository,
    AuthenticationManager authManager,
    PasswordEncoder passwordEncoder) {
  /* ----------------------------------------------------------------------------------------------------------- */
    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.authManager = authManager;
    this.passwordEncoder = passwordEncoder;
  }

  public String authenticate(AuthDTO authDTO) throws AuthenticationException {

    Authentication auth = new UsernamePasswordAuthenticationToken(
      authDTO.username(),
      authDTO.password()
    );

    /* Authenticate the user. If credential has not correct throws AuthenticationException */
    authManager.authenticate(auth);

    User user = userRepository.findByUsername(authDTO.username())
      .orElseThrow(()-> new UsernameNotFoundException("Bad Credentials"));

    return jwtService.createToken(user);
  }

  public User register(RegisterDTO registerDTO){

    User NUser = new User(
      registerDTO.username(),
      registerDTO.name(),
      passwordEncoder.encode(registerDTO.password()),
      UserRole.USER
    );

    return userRepository.save(NUser);
  }
}
