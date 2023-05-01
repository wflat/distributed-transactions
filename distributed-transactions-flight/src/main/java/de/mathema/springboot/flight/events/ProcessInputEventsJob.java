package de.mathema.springboot.flight.events;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.flight.repository.FlightInfo;
import de.mathema.springboot.flight.repository.FlightInfoNotFoundException;
import de.mathema.springboot.flight.repository.FlightInfoRepository;
import de.mathema.springboot.flight.repository.FlightInputRepository;
import de.mathema.springboot.flight.repository.FlightOutput;
import de.mathema.springboot.flight.repository.FlightOutputRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@RequiredArgsConstructor
@Transactional(TxType.REQUIRED)
@Slf4j
@Component
@Profile("!test")
public class ProcessInputEventsJob {

  private final FlightInfoRepository flightInfoRepository;
  private final FlightInputRepository flightInputRepository;
  private final FlightOutputRepository flightOutputRepository;
  private final JsonService jsonService;

  @Scheduled(initialDelayString = "PT0S", fixedDelay = 100)
  @SchedulerLock(name = "ProcessInputEventsJob.process")
  public void process() {
    log.debug("Start processing ...");

    flightInputRepository.getFirstEvent().ifPresent(inputEvent -> {
      log.info("Process InputEvent: {}/{}",
          inputEvent.getId(), inputEvent.getCreated().toString());

      final EntityEvent event = jsonService.objekt(inputEvent.getEventJson(), EntityEvent.class);
      log.info("Process EntityEvent: {}/{}/{}/{}",
          event.getEventId(), event.getEventType(), event.getEntityId(), event.getEntityQuantity());

      try {
        final FlightInfo flightInfo = getFlightInfo(event.getEntityId());
        switch (event.getEventType()) {
          case BOOK -> handleBookEvent(event, flightInfo);
          case CANCEL -> handleCancelEvent(event, flightInfo);
          default -> handleUnexpectedEvent(event);
        }
      } catch (final Exception exc) {
        log.error("Exception: {}", exc.getMessage());
        createFailureEvent(event, exc.getMessage());
      }
      inputEvent.setProcessed(true);
    });

    log.debug("End processing");
  }

  void handleBookEvent(final EntityEvent event, final FlightInfo flightInfo) {
    final int newQuantity = flightInfo.getQuantity() - event.getEntityQuantity();

    if (newQuantity < 0) {
      createFailureEvent(event, String.format("Not enough quantity : requested [%d] > actual [%d]",
          event.getEntityQuantity(), flightInfo.getQuantity()));
    } else {
      log.info("Flight {} booked successful : old quantity [{}] => new quantity [{}]",
          flightInfo.getId(), flightInfo.getQuantity(), newQuantity);

      flightInfo.setQuantity(newQuantity);
      createOutputEvent(EventType.CONFIRMATION, event.getTravelBookingId(), event.getEntityId(),
          event.getEntityQuantity(), null);
    }
  }

  void handleCancelEvent(final EntityEvent event, final FlightInfo flightInfo) {
    final int newQuantity = flightInfo.getQuantity() + event.getEntityQuantity();

    log.info("Flight {} canceled successful : old quantity [{}] => new quantity [{}]",
        flightInfo.getId(), flightInfo.getQuantity(), newQuantity);

    flightInfo.setQuantity(newQuantity);
    createOutputEvent(EventType.CANCEL_CONFIRMATION, event.getTravelBookingId(), event.getEntityId(),
        event.getEntityQuantity(), null);
  }

  void handleUnexpectedEvent(final EntityEvent event) {
    log.warn("Unexpected FlightEvent received {}/{} for TravelBooking {}",
        event.getEventId(), event.getEventType(), event.getTravelBookingId());
  }

  void createFailureEvent(final EntityEvent event, final String message) {
    createOutputEvent(EventType.FAILURE, event.getTravelBookingId(), event.getEntityId(),
        event.getEntityQuantity(), message);
  }

  void createOutputEvent(final EventType eventType, final String travelBookingId,
                         final String entityId, final int entityQuantity, final String message) {
    log.info("Create OutputEvent: {}/{}/{}/{} for TravelBooking {}",
        eventType, entityId, entityQuantity, message, travelBookingId);

    final FlightOutput output = FlightOutput.builder()
        .build();
    output.prePersist();
    final EntityEvent entityEvent = EntityEvent.builder()
        .eventId(output.getId())
        .eventType(eventType)
        .travelBookingId(travelBookingId)
        .entityId(entityId)
        .entityQuantity(entityQuantity)
        .message(message)
        .build();
    output.setEventJson(jsonService.json(entityEvent));
    flightOutputRepository.save(output);
  }

  FlightInfo getFlightInfo(final String id) {
    return flightInfoRepository.findById(id)
        .orElseThrow(() -> new FlightInfoNotFoundException(String.format("FlightInfo with id %s not found", id)));
  }
}
