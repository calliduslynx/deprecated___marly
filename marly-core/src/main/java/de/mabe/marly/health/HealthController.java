package de.mabe.marly.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  @GetMapping("/info")
  public String statusInfo() {
    return "{ \"status\" : \"active\" }";
  }
}
