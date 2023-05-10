package com.mikellbobadilla.jwt_security.exception;

public class ApiRequestException extends RuntimeException {
  public ApiRequestException(String message) {
    super(message);
  }
}
