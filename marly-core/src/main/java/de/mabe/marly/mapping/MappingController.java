package de.mabe.marly.mapping;

import de.mabe.marly.security.WebSecurityConfig.OAuth2User;
import de.mabe.marly.user.User;
import static java.util.stream.Collectors.toList;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingController {
  @Autowired private Mapping.Repo mappingRepo;
  @Autowired private User.Repo userRepo;
  @Autowired private MappingService mappingService;

  private static class MappingInfo {
    public final String shortUrl, longUrl, userEmail;
    public final int lastHour, lastDay, lastMonth, total;

    public MappingInfo(String shortUrl, String longUrl, String userEmail, int lastHour, int lastDay, int lastMonth, int total) {
      this.shortUrl = shortUrl;
      this.longUrl = longUrl;
      this.userEmail = userEmail;
      this.lastHour = lastHour;
      this.lastDay = lastDay;
      this.lastMonth = lastMonth;
      this.total = total;
    }
  }

  /**
   * returns mappings in a native format 'cause json is unnecessary verbose
   */
  @GetMapping("/mappings")
  public List<MappingInfo> getMappingsForUser(OAuth2User sessionUser) {

    System.out.println(sessionUser);

    List<Mapping> mappings;
    if (sessionUser.isAdmin()) {
      mappings = mappingRepo.findAll();
    } else {
      User user = userRepo.findByEmail(sessionUser.getEmail());
      if (user == null) return Collections.emptyList();
      mappings = mappingRepo.findByUser(user);
    }

    return mappings.stream().map(mapping -> {
      return new MappingInfo(
          mappingService.shortUrlPostfixToHref(mapping.getShortUrl()),
          mapping.getLongUrl(),
          mapping.getUser().getEmail(),
          0, 0, 0, 0 // FIXME: load numbers
      );
    }).collect(toList());
  }

  public static final class NewMappingModel {
    public String url;
  }

  @PostMapping("/mappings")
  public ResponseEntity<Void> createNewMapping(Principal principal, @RequestBody NewMappingModel model) {
    String email = new OAuth2User(principal).getEmail();
    mappingService.createNewMapping(email, model.url);
    return ResponseEntity.status(201).build();
  }

  @DeleteMapping("/mappings/{shortUrl}")
  public ResponseEntity<Void> deleteMapping(Principal principal, @RequestBody @PathVariable("shortUrl") String shortUrl) {
    OAuth2User user = new OAuth2User(principal);
    Mapping mapping = mappingService.getMappingByShortUrl(shortUrl);
    if (mapping == null)
      return ResponseEntity.notFound().build();
    if (!user.isAdmin() && !user.getEmail().equals(mapping.getUser().getEmail()))
      return ResponseEntity.status(403).build();

    mappingService.deleteMapping(mapping);
    return ResponseEntity.status(200).build();
  }

  /**
   * returns mappings in a native format 'cause json is unnecessary verbose
   */
  @GetMapping("/intern/mappings")
  public String getAllMappings() {
    return mappingRepo.findAll().stream()
        .map(mapping -> mapping.getShortUrl() + "<>" + mapping.getLongUrl())
        .collect(Collectors.joining("><"));
  }
}
