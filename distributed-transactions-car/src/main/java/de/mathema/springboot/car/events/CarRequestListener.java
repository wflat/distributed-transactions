package de.mathema.springboot.car.events;

import de.mathema.springboot.car.repository.CarInputRepository;
import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.service.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarRequestListener {

  private final CarInputRepository carInputRepository;
  private final JsonService jsonService;

  @Transactional
  @JmsListener(destination = "${car.activemq.input-queue}", containerFactory = "jmsListenerContainerFactory")
  public void neuerUmsatzEmpfangen(final Message<EntityEvent> msg) {
    final EntityEvent event = msg.getPayload();
    log.info("Received CarEvent {}/{} for TravelBooking {}",
        event.getEventId(), event.getEventType(), event.getTravelBookingId());

    // TODO: CarInput Nachricht erstellen und in carInputRepository ablegen
  }
}
