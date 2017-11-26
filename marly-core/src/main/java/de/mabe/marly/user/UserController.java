package de.mabe.marly.user;

import de.mabe.marly.security.OAuth2User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @Autowired private UserService userService;

  private static class UserInfo {
    public final String name, imageurl;
    public final boolean isAdmin;

    public UserInfo(String name, boolean isAdmin, String imageurl) {
      this.name = name;
      this.isAdmin = isAdmin;
      this.imageurl = imageurl;
    }
  }

  /**
   * retrieves currently logged in username or null
   */
  @GetMapping("/user")
  public UserInfo user(OAuth2User user) {
    return user.isAuthenticated() ? new UserInfo(user.getName(), user.isAdmin(), user.getImageUrl()) : null;
  }
}
