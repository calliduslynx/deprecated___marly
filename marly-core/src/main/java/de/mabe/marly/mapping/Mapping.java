package de.mabe.marly.mapping;

import de.mabe.marly.user.User;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Entity
public class Mapping {
  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true, nullable = false)
  private String tiny;

  @Column(nullable = false)
  private String url;

  @ManyToOne(optional = false)
  private User user;

  Mapping() {
    // hibernate
  }

  public Mapping(String tiny, String url, User user) {
    this.tiny = tiny;
    this.url = url;
    this.user = user;
  }

  public String getTiny() {
    return tiny;
  }

  public String getUrl() {
    return url;
  }

  public User getUser() {
    return user;
  }

  @Override
  public String toString() {
    return tiny + " -> " + url;
  }

  // ******************************************************************************************************************

  @Repository
  public interface Repo extends JpaRepository<Mapping, Long> {
    Mapping findByTiny(String tiny);

    List<Mapping> findByUser(User user);
  }
}
