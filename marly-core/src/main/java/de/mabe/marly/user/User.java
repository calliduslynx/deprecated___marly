package de.mabe.marly.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Entity
public class User {
  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true, nullable = false)
  private String email;

  User() {
    // hibernate
  }

  public User(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }


  @Repository
  public interface Repo extends JpaRepository<User, Long> {
    User findByEmail(String email);
  }
}
