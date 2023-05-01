package de.mathema.springboot.travel.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "travel__booking")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TravelBooking {

  public static final int TEXT_LENGTH = 2000;

  @Id
  @Column(name = "id")
  private String id;
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private TravelStatus status;
  @Column(name = "status_details", length = TEXT_LENGTH)
  private String statusDetails;
  @Column(name = "created")
  private LocalDateTime created;
  @Column(name = "updated")
  private LocalDateTime updated;
  @Column(name = "process_instance_id")
  private String processInstanceId;
  @Column(name = "hotel_id")
  private String hotelId;
  @Column(name = "hotel_quantity")
  private int hotelQuantity;
  @Column(name = "hotel_approved")
  private boolean hotelApproved;
  @Column(name = "car_id")
  private String carId;
  @Column(name = "car_quantity")
  private int carQuantity;
  @Column(name = "car_approved")
  private boolean carApproved;
  @Column(name = "flight_id")
  private String flightId;
  @Column(name = "flight_quantity")
  private int flightQuantity;
  @Column(name = "flight_approved")
  private boolean flightApproved;
  @Version
  @Column(name = "version")
  private int version = 0;

  @PrePersist
  public void prePersist() {
    if (id == null) {
      id = "TRAVEL-BOOKING-" + UUID.randomUUID();
    }
    created = LocalDateTime.now();
    updated = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    updated = LocalDateTime.now();
    if (hotelApproved && carApproved && flightApproved) {
      status = TravelStatus.BOOKED;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    return Objects.equals(id, ((TravelBooking) o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
