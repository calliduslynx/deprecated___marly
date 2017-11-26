package de.mabe.marly;

import de.mabe.marly.mapping.Mapping;
import de.mabe.marly.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class MarlyApplication {
  public static void main(String[] args) {
    SpringApplication.run(MarlyApplication.class, args);
  }

  @Autowired private User.Repo userRepo;
  @Autowired private Mapping.Repo mappingRepo;

//  @PostConstruct
//  public void prepareTestData() {
//    User user = userRepo.save(new User("callidus.lynx@googlemail.com"));
//    mappingRepo.save(new Mapping("abcd1", "https://www.heise.de", user));
//    mappingRepo.save(new Mapping("abcd2", "https://www.golem.de", user));
//    mappingRepo.save(new Mapping("abcd3", "https://www.stackoverflow.com", user));
//  }
}
