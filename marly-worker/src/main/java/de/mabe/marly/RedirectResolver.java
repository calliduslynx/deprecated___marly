package de.mabe.marly;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import com.mashape.unirest.http.Unirest;

public class RedirectResolver {
  static Logger log = Log.getLogger(RedirectResolver.class);


  private final String redirectResolveUrl;

  public RedirectResolver(String redirectResolveUrl) {
    this.redirectResolveUrl = redirectResolveUrl;
  }

  public Map<String, String> resolve() {
    try {
      String fixedString = Unirest.get(redirectResolveUrl).asString().getBody();
      return Arrays
          .stream(fixedString.split("><"))
          .map(it -> it.split("<>"))
          .collect(Collectors.toMap(it -> it[0], it -> it[1]));
    } catch (Exception e) {
      log.warn("Error resolving Mapping: " + e.getMessage());
      return null;
    }
  }
}
