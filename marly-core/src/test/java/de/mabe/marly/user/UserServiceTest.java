package de.mabe.marly.user;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {
  @Autowired UserService userService;

  @Autowired private User.Repo userRepo;

  @Test
  public void user_is_created_if_it_didnt_exist_before() {
    assertEquals(0, userRepo.count());

    userService.createUserIfNotExist("a@b.c");

    assertEquals(1, userRepo.count());
  }

  @Test
  public void user_is_not_created_if_it_did_exist_before() {
    userRepo.save(new User("a@b.c"));
    assertEquals(1, userRepo.count());

    userService.createUserIfNotExist("a@b.c");

    assertEquals(1, userRepo.count());
  }
}
