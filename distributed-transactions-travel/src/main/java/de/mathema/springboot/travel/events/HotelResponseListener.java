package de.mathema.springboot.travel.events;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.travel.repository.EntityType;
import de.mathema.springboot.travel.repository.TravelInput;
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
public class HotelResponseListener {

  private final TravelInputRepository travelInputRepository;
  private final JsonService jsonService;

  @Transactional
  @JmsListener(destination = "${travel.activemq.hotel.output-queue}", containerFactory = "jmsListenerContainerFactory")
  public void neuerUmsatzEmpfangen(final Message<EntityEvent> msg) {
    final EntityEvent event = msg.getPayload();
    log.info("Received HotelEvent {}/{} for TravelBooking {}",
        event.getEventId(), event.getEventType(), event.getTravelBookingId());

    final TravelInput inputEvent = TravelInput.builder()
        .id(event.getEventId())
        .entityType(EntityType.HOTEL)
        .eventJson(jsonService.json(event))
        .build();
    travelInputRepository.saveTravelInput(inputEvent);
  }
}
