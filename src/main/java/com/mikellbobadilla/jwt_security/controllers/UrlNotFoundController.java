package com.mikellbobadilla.jwt_security.controllers;

import com.mikellbobadilla.jwt_security.DTO.NotFoundUrlDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.logging.Logger;

@RestController
public class UrlNotFoundController {
  private static final Logger logger = Logger.getLogger(UrlNotFoundController.class.getName());
  @RequestMapping("*")
  public ResponseEntity<NotFoundUrlDTO> handleNotFoundException(HttpServletRequest request){
    logger.warning(String.format("url: '%s' not found!.", request.getRequestURI()));
    NotFoundUrlDTO notFoundUrlDTO = new NotFoundUrlDTO(
      HttpStatus.NOT_FOUND,
      "Page Not Found!",
      new Date(),
      request.getRequestURI()
    );
    return new ResponseEntity<>(notFoundUrlDTO, notFoundUrlDTO.httpStatus());
  }

}
