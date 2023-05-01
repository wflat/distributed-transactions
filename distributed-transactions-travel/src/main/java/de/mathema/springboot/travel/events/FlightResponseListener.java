package de.mathema.springboot.travel.events;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.travel.repository.TravelInputRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlightResponseListener {

  private final TravelInputRepository travelInputRepository;
  private final JsonService jsonService;

  @Transactional
  @JmsListener(destination = "${travel.activemq.flight.output-queue}", containerFactory = "jmsListenerContainerFactory")
  public void neuerUmsatzEmpfangen(final Message<EntityEvent> msg) {
    final EntityEvent event = msg.getPayload();
    log.info("Received FlightEvent {}/{} for TravelBooking {}",
        event.getEventId(), event.getEventType(), event.getTravelBookingId());

    // TODO: TravelInput Flight-Nachricht erstellen und in travelInputRepository ablegen
  }
}
