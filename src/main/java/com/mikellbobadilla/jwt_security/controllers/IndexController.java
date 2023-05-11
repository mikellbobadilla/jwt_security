package com.mikellbobadilla.jwt_security.controllers;

import com.mikellbobadilla.jwt_security.DTO.IndexDTO;
import com.mikellbobadilla.jwt_security.DTO.NotFoundUrlDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class IndexController {

  @GetMapping("/")
  public ResponseEntity<IndexDTO> notFoundUrl(){
    return new ResponseEntity<>(new IndexDTO("Principal Page"), HttpStatus.OK);
  }
}
