package de.mabe.marly.user;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private static final Logger log = Logger.getLogger(UserService.class);

  @Autowired private User.Repo userRepo;

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
