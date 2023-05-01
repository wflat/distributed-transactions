package de.mathema.springboot.hotel.events;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.hotel.repository.HotelInput;
import de.mathema.springboot.hotel.repository.HotelInputRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class HotelRequestListener {

  private final HotelInputRepository hotelInputRepository;
  private final JsonService jsonService;

  @Transactional
  @JmsListener(destination = "${hotel.activemq.input-queue}", containerFactory = "jmsListenerContainerFactory")
  public void neuerUmsatzEmpfangen(final Message<EntityEvent> msg) {
    final EntityEvent event = msg.getPayload();
    log.info("Received HotelEvent {}/{} for TravelBooking {}",
        event.getEventId(), event.getEventType(), event.getTravelBookingId());

    final HotelInput input = HotelInput.builder()
        .id(event.getEventId())
        .eventJson(jsonService.json(event))
        .build();
    hotelInputRepository.saveHotelInput(input);
  }
}
