package de.mabe.marly.mapping;

import de.mabe.marly.user.User;
import nl.flotsam.xeger.Xeger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MappingService {
  private static final Logger log = Logger.getLogger(MappingService.class);

  public static final String SHORT_URL_PATTERN = "[a-zA-Z0-9]{5}";

  private final Xeger xeger = new Xeger(SHORT_URL_PATTERN);

  @Autowired private User.Repo userRepo;
  @Autowired private Mapping.Repo mappingRepo;

  public String shortUrlPostfixToHref(String postfix) {
    return "http://localhost:9010/" + postfix;
  }

  public synchronized void createNewMapping(String email, String longUrl) {
    User user = userRepo.findByEmail(email);
    if (user == null) throw new IllegalStateException("user not found");

    String shortUrl = createNewShortUrl();

    Mapping mapping = new Mapping(shortUrl, longUrl, user);
    mappingRepo.save(mapping);

    log.info("stored " + mapping);
  }

  private String createNewShortUrl() {
    while (true) {
      String newShortUrl = xeger.generate();

      // checking if url already exists
      Mapping mapping = mappingRepo.findByShortUrl(newShortUrl);
      if (mapping == null)
        return newShortUrl;
    }
  }


  public Mapping getMappingByShortUrl(String shortUrl) {
    return mappingRepo.findByShortUrl(shortUrl);
  }

  public void deleteMapping(Mapping mapping) {
    log.info("deleting " + mapping);
    mappingRepo.delete(mapping);
  }
}
