package de.mabe.marly.security;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public class OAuth2User {
  private final String name, email, imageUrl;
  private final boolean isAuthenticated, isAdmin;

  public OAuth2User() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication instanceof AnonymousAuthenticationToken) {
      isAuthenticated = false;
      isAdmin = false;
      name = email = imageUrl = "";
    } else {
      isAuthenticated = true;
      Authentication userAuthentication = ((OAuth2Authentication) authentication).getUserAuthentication();
      Map<String, String> details = (Map<String, String>) userAuthentication.getDetails();

      name = authentication.getName();
      email = details.get("email");
      imageUrl = details.get("picture");

      boolean isAdmin = false;
      for (GrantedAuthority authority : authentication.getAuthorities()) {
        if ("ROLE_ADMIN".equals(authority.getAuthority())) isAdmin = true;
      }
      this.isAdmin = isAdmin;
    }
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public boolean isAuthenticated() {
    return isAuthenticated;
  }

  @Override
  public String toString() {
    return name + " (" + email + ")";
  }
}
