package de.mabe.marly.user;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private static final Logger log = Logger.getLogger(UserService.class);

  @Autowired private User.Repo userRepo;

  private final List<String> adminUsers;

  UserService(@Value("${marly.admin-users:}") String adminUsersConfig) {
    adminUsers = Arrays.stream(adminUsersConfig.split(","))
        .map(String::trim)
        .filter(user -> !user.isEmpty())
        .collect(toList());

    log.info("admins configured: " + adminUsers);
  }

  public boolean isAdmin(String name) {
    return adminUsers.contains(name);
  }

  public void createUserIfNotExist(String email) {
    User user = userRepo.findByEmail(email);
    if (user == null) {
      userRepo.save(new User(email));
      log.info("created user: " + email);
    }
  }

  public User findByEmail(String email) {
    return userRepo.findByEmail(email);
  }
}
