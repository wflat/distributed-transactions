package de.mathema.springboot.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityEvent {

  private String eventId;
  private String travelBookingId;
  private EventType eventType;
  private String entityId;
  private int entityQuantity;
  private String message;
}
