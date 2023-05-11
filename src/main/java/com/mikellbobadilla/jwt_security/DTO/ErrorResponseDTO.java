package com.mikellbobadilla.jwt_security.DTO;

import org.springframework.http.HttpStatus;

import java.util.Date;

public record ErrorResponseDTO(
  HttpStatus status,
  String message,
  Date timestamp
) {
}
