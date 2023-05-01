package de.mathema.springboot.travel.service;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TravelBooking {

  private String id;
  private String status;
  private String statusDetails;
  private LocalDateTime created;
  private LocalDateTime updated;
  private String processInstanceId;
  private String hotelId;
  private int hotelQuantity;
  private boolean hotelApproved;
  private String carId;
  private int carQuantity;
  private boolean carApproved;
  private String flightId;
  private int flightQuantity;
  private boolean flightApproved;
}
