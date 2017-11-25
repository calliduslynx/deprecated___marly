package de.mabe.marly;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mashape.unirest.http.Unirest;

public class RedirectEventThrower {
  private static final ExecutorService executorService = new ThreadPoolExecutor(/* min-size */10, /* max-size */ 200, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
  private String redirectEventUrl;

  public RedirectEventThrower(String redirectEventUrl) {
    this.redirectEventUrl = redirectEventUrl;
  }

  public void throwUrlCalledEvent(String shortUrl) {
    executorService.submit(() -> {
      try {
        Unirest.post(redirectEventUrl + "/" + shortUrl).asString();
      } catch (Exception e) {
        MarlyWorker.log.warn("Error sending redirectCalledEvent: " + e.getMessage());
      }
    });
  }
}
