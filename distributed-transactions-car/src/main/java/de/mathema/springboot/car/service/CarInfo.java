package de.mathema.springboot.car.service;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarInfo {

  private String id;
  private String description;
  private LocalDateTime created;
  private LocalDateTime updated;
  private int quantity;
}
