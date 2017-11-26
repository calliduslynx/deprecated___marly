package de.mabe.marly.mapping;

import de.mabe.marly.security.OAuth2User;
import de.mabe.marly.user.User;
import de.mabe.marly.user.UserService;
import static java.util.stream.Collectors.toList;

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
  @Autowired private UserService userService;
  @Autowired private MappingService mappingService;

  private static class MappingInfo {
    public final String tiny, tinyUrl, url, userEmail;

    public MappingInfo(String tiny, String tinyUrl, String url, String userEmail) {
      this.tiny = tiny;
      this.tinyUrl = tinyUrl;
      this.url = url;
      this.userEmail = userEmail;
    }
  }

  /**
   * returns mappings in a native format 'cause json is unnecessary verbose
   */
  @GetMapping("/mappings")
  public List<MappingInfo> getMappingsForUser(OAuth2User sessionUser) {
    List<Mapping> mappings;
    if (sessionUser.isAdmin()) {
      mappings = mappingService.findAll();
    } else {
      User user = userService.findByEmail(sessionUser.getEmail());
      if (user == null) return Collections.emptyList();
      mappings = mappingService.findByUser(user);
    }

    return mappings.stream().map(mapping -> {
      return new MappingInfo(
          mapping.getTiny(),
          mappingService.getTinyUrl(mapping.getTiny()),
          mapping.getUrl(),
          mapping.getUser().getEmail()
      );
    }).collect(toList());
  }

  public static final class NewMappingModel {
    public String url;
  }

  @PostMapping("/mappings")
  public ResponseEntity<Void> createNewMapping(OAuth2User user, @RequestBody NewMappingModel model) {
    String email = user.getEmail();
    mappingService.createNewMapping(email, model.url);
    return ResponseEntity.status(201).build();
  }

  @DeleteMapping("/mappings/{tiny}")
  public ResponseEntity<Void> deleteMapping(OAuth2User user, @RequestBody @PathVariable("tiny") String tiny) {

    Mapping mapping = mappingService.getMappingByTiny(tiny);
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
    return mappingService.findAll().stream()
        .map(mapping -> mapping.getTiny() + "<>" + mapping.getUrl())
        .collect(Collectors.joining("><"));
  }
}
