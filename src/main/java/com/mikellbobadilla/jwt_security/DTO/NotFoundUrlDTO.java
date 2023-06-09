package com.mikellbobadilla.jwt_security.DTO;

import org.springframework.http.HttpStatus;

import java.util.Date;

public record NotFoundUrlDTO(
  HttpStatus httpStatus,
  String message,
  Date timestamp,
  String url) {
}
