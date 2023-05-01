package de.mathema.springboot.hotel.service;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HotelInfo {

  private String id;
  private String description;
  private LocalDateTime created;
  private LocalDateTime updated;
  private int quantity;
}
