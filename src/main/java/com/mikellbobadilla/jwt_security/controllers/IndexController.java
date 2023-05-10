package com.mikellbobadilla.jwt_security.controllers;

import com.mikellbobadilla.jwt_security.DTO.NotFoundUrlDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("*")
public class IndexController {


  @GetMapping
  public ResponseEntity<NotFoundUrlDTO> notFoundUrl(){
    return new ResponseEntity<>(new NotFoundUrlDTO("Page Not Found!"), HttpStatus.NOT_FOUND);
  }
}
