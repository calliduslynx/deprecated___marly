package de.mabe.marly.mapping;

import de.mabe.marly.statistic.RedirectEvent;
import de.mabe.marly.user.User;
import de.mabe.marly.user.UserService;
import nl.flotsam.xeger.Xeger;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MappingService {
  private static final Logger log = Logger.getLogger(MappingService.class);

  private final Xeger xeger;

  @Autowired private UserService userService;
  @Autowired private RedirectEvent.Repo eventRepo;
  @Autowired private Mapping.Repo mappingRepo;

  private MappingService(@Value("${marly.tiny-pattern}") String tinyPattern) {
    log.info("using tiny pattern: " + tinyPattern);
    this.xeger = new Xeger(tinyPattern);
  }

  public String getTinyUrl(String tiny) {
    return "http://localhost:9010/" + tiny;
  }

  public synchronized void createNewMapping(String email, String url) {
    User user = userService.findByEmail(email);
    if (user == null) throw new IllegalStateException("user not found");

    String tiny = generateNewTiny();

    Mapping mapping = new Mapping(tiny, url, user);
    mappingRepo.save(mapping);

    log.info("stored " + mapping);
  }

  private String generateNewTiny() {
    while (true) {
      String newShortUrl = xeger.generate();

      // checking if url already exists
      Mapping mapping = mappingRepo.findByTiny(newShortUrl);
      if (mapping == null)
        return newShortUrl;
    }
  }


  public Mapping getMappingByTiny(String tiny) {
    return mappingRepo.findByTiny(tiny);
  }

  public void deleteMapping(Mapping mapping) {
    log.info("deleting " + mapping);
    eventRepo.delete(eventRepo.findByMapping(mapping));
    mappingRepo.delete(mapping);
  }

  public List<Mapping> findAll() {
    return mappingRepo.findAll();
  }

  public List<Mapping> findByUser(User user) {
    return mappingRepo.findByUser(user);
  }
}
