package de.mabe.marly.statistic;

import de.mabe.marly.mapping.Mapping;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


// FIXME Tests
@RestController
public class StatisticController {
  private static final Logger log = Logger.getLogger(StatisticController.class);

  @Autowired private Mapping.Repo mappingRepo;
  @Autowired private RedirectEvent.Repo eventRepo;

  @PostMapping("/intern/statistic/redirect_event/{shortUrl}")
  public ResponseEntity<Void> catchRedirectEvent(@PathVariable("shortUrl") String shortUrl) {
    Mapping mapping = mappingRepo.findByShortUrl(shortUrl);
    if (mapping != null) {
      eventRepo.save(new RedirectEvent(mapping));
      return ResponseEntity.accepted().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
