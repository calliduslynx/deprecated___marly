package de.mabe.marly.health;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HealthControllerTest {

  @Test
  public void healthContoller_always_returns_status_is_active() {
    assertEquals(true, new HealthController().statusInfo().active);
  }

}