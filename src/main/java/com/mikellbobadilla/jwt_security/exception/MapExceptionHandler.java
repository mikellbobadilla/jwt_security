package com.mikellbobadilla.jwt_security.exception;

import com.mikellbobadilla.jwt_security.DTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.logging.Logger;

@RestControllerAdvice
public class MapExceptionHandler {
  private static final Logger log = Logger.getLogger(MapExceptionHandler.class.getName());

  /* Default Exception */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> defaultException(Exception exc){
    var status = HttpStatus.INTERNAL_SERVER_ERROR;
    var response = getResponseException(status, "Internal Server Error");
    log.severe("""
      {
        Log: Server response %s,
        MessageException: %s
        Cause: %s,
      }
      """.formatted(status.value(), exc.getMessage(), exc.getClass().getName())
    );
    return new ResponseEntity<>(response, status);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponseDTO> authException(AuthenticationException exc){
    String message;
    var status = HttpStatus.UNAUTHORIZED;
    message = exc.getMessage();
    if(exc instanceof UsernameNotFoundException){
      message = "Bad Credentials!";
    }
    log.warning("""
      {
        Log: Server response %s,
        MessageException: %s
        Cause: %s,
      }
      """.formatted(status.value(), exc.getMessage(), exc.getClass().getName())
    );
    return new ResponseEntity<>(getResponseException(status, message), status);
  }

  private ErrorResponseDTO getResponseException(HttpStatus status, String message){
    return new ErrorResponseDTO(status, message, new Date());
  }
}
