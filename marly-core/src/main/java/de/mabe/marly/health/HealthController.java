package de.mabe.marly.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  public static final class StatusInfo {
    public final boolean active;

    public StatusInfo(boolean active) {
      this.active = active;
    }
  }

  @GetMapping("/info")
  public StatusInfo statusInfo() {
    return new StatusInfo(true);
  }
}
