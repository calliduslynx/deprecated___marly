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
  private String shortUrl;

  @Column(nullable = false)
  private String longUrl;

  @ManyToOne(optional = false)
  private User user;

  Mapping() {
    // hibernate
  }

  public Mapping(String shortUrl, String longUrl, User user) {
    this.shortUrl = shortUrl;
    this.longUrl = longUrl;
    this.user = user;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public String getLongUrl() {
    return longUrl;
  }

  public User getUser() {
    return user;
  }

  @Override
  public String toString() {
    return shortUrl + " -> " + longUrl;
  }

  // ******************************************************************************************************************

  @Repository
  public interface Repo extends JpaRepository<Mapping, Long> {
    Mapping findByShortUrl(String shortUrl);

    List<Mapping> findByUser(User user);
  }
}
