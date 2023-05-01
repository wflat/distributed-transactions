package de.mathema.springboot.flight.service;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FlightInfo {

  private String id;
  private String description;
  private LocalDateTime created;
  private LocalDateTime updated;
  private int quantity;
}
