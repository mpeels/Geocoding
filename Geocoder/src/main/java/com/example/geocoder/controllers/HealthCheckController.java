package com.example.geocoder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/health")
public class HealthCheckController {

  @GetMapping()
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Yes, this application is still alive!");
  }
}
