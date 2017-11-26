package de.mabe.marly.statistic;

import de.mabe.marly.mapping.Mapping;
import de.mabe.marly.mapping.MappingService;
import de.mabe.marly.security.OAuth2User;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StatisticController {
  private static final Logger log = Logger.getLogger(StatisticController.class);

  private static final long ONE_HOUR = 60L * 60 * 1000;
  private static final long ONE_DAY = 24L * ONE_HOUR;
  private static final long ONE_WEEK = 7L * ONE_DAY;


  @Autowired private MappingService mappingService;
  @Autowired private RedirectEvent.Repo eventRepo;

  @PostMapping("/intern/statistic/redirect_event/{tiny}")
  public ResponseEntity<Void> catchRedirectEvent(@PathVariable("tiny") String tiny) {
    Mapping mapping = mappingService.getMappingByTiny(tiny);
    if (mapping != null) {
      eventRepo.save(new RedirectEvent(mapping));
      return ResponseEntity.accepted().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  public static final class Statistics {
    public final int lastHour, last24Hours, last7Days, total;

    public Statistics(int lastHour, int last24Hours, int last7Days, int total) {
      this.lastHour = lastHour;
      this.last24Hours = last24Hours;
      this.last7Days = last7Days;
      this.total = total;
    }
  }


  @GetMapping("/statistic/{tiny}")
  public ResponseEntity<Statistics> getStatistics(OAuth2User user, @PathVariable("tiny") String tiny) {
    Mapping mapping = mappingService.getMappingByTiny(tiny);
    if (mapping == null)
      return ResponseEntity.notFound().build();

    if (!user.isAdmin() && !mapping.getUser().getEmail().equals(user.getEmail()))
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    return ResponseEntity.ok(new Statistics(
        eventRepo.countByMappingAndDateAfter(mapping, new Date(new Date().getTime() - ONE_HOUR)),
        eventRepo.countByMappingAndDateAfter(mapping, new Date(new Date().getTime() - ONE_DAY)),
        eventRepo.countByMappingAndDateAfter(mapping, new Date(new Date().getTime() - ONE_WEEK)),
        eventRepo.countByMapping(mapping)
    ));
  }
}
