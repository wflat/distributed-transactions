package de.mathema.springboot.flight.events;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.flight.repository.FlightInput;
import de.mathema.springboot.flight.repository.FlightInputRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlightRequestListener {

  private final FlightInputRepository flightInputRepository;
  private final JsonService jsonService;

  @Transactional
  @JmsListener(destination = "${flight.activemq.input-queue}", containerFactory = "jmsListenerContainerFactory")
  public void neuerUmsatzEmpfangen(final Message<EntityEvent> msg) {
    final EntityEvent event = msg.getPayload();
    log.info("Received FlightEvent {}/{} for TravelBooking {}",
        event.getEventId(), event.getEventType(), event.getTravelBookingId());

    final FlightInput input = FlightInput.builder()
        .id(event.getEventId())
        .eventJson(jsonService.json(event))
        .build();
    flightInputRepository.saveFlightInput(input);
    // TODO: FlightInput Nachricht erstellen und in flightInputRepository ablegen
  }
}
