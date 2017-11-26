package de.mabe.marly.statistic;

import de.mabe.marly.mapping.Mapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Entity
public class RedirectEvent {
  @Id
  @GeneratedValue
  private long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Mapping mapping;

  @Column(nullable = false)
  private Date date;


  RedirectEvent() {
    // Hibernate
  }

  RedirectEvent(Mapping mapping) {
    this.mapping = mapping;
    this.date = new Date();
  }

  @Override
  public String toString() {
    return "Event: " + new SimpleDateFormat("HH:mm:ss").format(date);
  }

  @Repository
  public interface Repo extends JpaRepository<RedirectEvent, Long> {
    int countByMapping(Mapping mapping);

    int countByMappingAndDateAfter(Mapping mapping, Date date);

    List<RedirectEvent> findByMapping(Mapping mapping);
  }
}
