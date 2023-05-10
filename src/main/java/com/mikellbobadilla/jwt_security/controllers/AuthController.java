package com.mikellbobadilla.jwt_security.controllers;

import com.mikellbobadilla.jwt_security.DTO.AuthDTO;
import com.mikellbobadilla.jwt_security.DTO.JwtDTO;
import com.mikellbobadilla.jwt_security.DTO.RegisterDTO;
import com.mikellbobadilla.jwt_security.DTO.UserDTO;
import com.mikellbobadilla.jwt_security.entities.User;
import com.mikellbobadilla.jwt_security.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final Logger logger = Logger.getLogger(AuthController.class.getName());

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/authenticate")
  public ResponseEntity<JwtDTO> authenticate(@RequestBody AuthDTO authDTO){
    String token = authService.authenticate(authDTO);
    logger.info("user authenticated!");
    return new ResponseEntity<>(new JwtDTO(token), HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO registerDTO){
    User createUser = authService.register(registerDTO);

    UserDTO createdUser = new UserDTO(
      createUser.getId(),
      createUser.getUsername(),
      createUser.getName(),
      createUser.getRole()
    );

    logger.info("user created!");
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }
}
