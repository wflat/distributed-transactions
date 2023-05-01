package de.mathema.springboot.travel.repository;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "travel__output")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TravelOutput {

  public static final int TEXT_LENGTH = 2000;

  @Id
  @Column(name = "id")
  private String id;
  @Column(name = "entity_type")
  @Enumerated(EnumType.STRING)
  private EntityType entityType;
  @Column(name = "processed")
  private boolean processed;
  @Column(name = "created")
  private LocalDateTime created;
  @Column(name = "updated")
  private LocalDateTime updated;
  @Column(name = "event_json", length = TEXT_LENGTH)
  private String eventJson;
  @Version
  @Column(name = "version")
  private int version = 0;

  @PrePersist
  public void prePersist() {
    if (id == null) {
      id = "TRAVEL-OUTPUT-EVENT-" + UUID.randomUUID();
    }
    created = LocalDateTime.now();
    updated = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    updated = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    return Objects.equals(id, ((TravelOutput) o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
