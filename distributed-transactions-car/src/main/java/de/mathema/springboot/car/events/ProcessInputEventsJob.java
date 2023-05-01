package de.mathema.springboot.car.events;

import de.mathema.springboot.car.repository.CarInfo;
import de.mathema.springboot.car.repository.CarInfoNotFoundException;
import de.mathema.springboot.car.repository.CarInfoRepository;
import de.mathema.springboot.car.repository.CarInputRepository;
import de.mathema.springboot.car.repository.CarOutput;
import de.mathema.springboot.car.repository.CarOutputRepository;
import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import de.mathema.springboot.common.service.JsonService;
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

  private final CarInfoRepository carInfoRepository;
  private final CarInputRepository carInputRepository;
  private final CarOutputRepository carOutputRepository;
  private final JsonService jsonService;

  @Scheduled(initialDelayString = "PT0S", fixedDelay = 100)
  @SchedulerLock(name = "ProcessInputEventsJob.process")
  public void process() {
    log.debug("Start processing ...");

    carInputRepository.getFirstEvent().ifPresent(inputEvent -> {
      log.info("Process InputEvent: {}/{}",
          inputEvent.getId(), inputEvent.getCreated().toString());

      final EntityEvent event = jsonService.objekt(inputEvent.getEventJson(), EntityEvent.class);
      log.info("Process EntityEvent: {}/{}/{}/{}",
          event.getEventId(), event.getEventType(), event.getEntityId(), event.getEntityQuantity());

      try {
        final CarInfo carInfo = getCarInfo(event.getEntityId());
        switch (event.getEventType()) {
          case BOOK -> handleBookEvent(event, carInfo);
          case CANCEL -> handleCancelEvent(event, carInfo);
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

  void handleBookEvent(final EntityEvent event, final CarInfo carInfo) {
    final int newQuantity = carInfo.getQuantity() - event.getEntityQuantity();

    if (newQuantity < 0) {
      createFailureEvent(event, String.format("Not enough quantity : requested [%d] > actual [%d]",
          event.getEntityQuantity(), carInfo.getQuantity()));
    } else {
      log.info("Car {} booked successful : old quantity [{}] => new quantity [{}]",
          carInfo.getId(), carInfo.getQuantity(), newQuantity);

      carInfo.setQuantity(newQuantity);
      createOutputEvent(EventType.CONFIRMATION, event.getTravelBookingId(), event.getEntityId(),
          event.getEntityQuantity(), null);
    }
  }

  void handleCancelEvent(final EntityEvent event, final CarInfo carInfo) {
    final int newQuantity = carInfo.getQuantity() + event.getEntityQuantity();

    log.info("Car {} canceled successful : old quantity [{}] => new quantity [{}]",
        carInfo.getId(), carInfo.getQuantity(), newQuantity);

    carInfo.setQuantity(newQuantity);
    createOutputEvent(EventType.CANCEL_CONFIRMATION, event.getTravelBookingId(), event.getEntityId(),
        event.getEntityQuantity(), null);
  }

  void handleUnexpectedEvent(final EntityEvent event) {
    log.warn("Unexpected CarEvent received {}/{} for TravelBooking {}",
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

    final CarOutput output = CarOutput.builder()
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
    carOutputRepository.save(output);
  }

  CarInfo getCarInfo(final String id) {
    return carInfoRepository.findById(id)
        .orElseThrow(() -> new CarInfoNotFoundException(String.format("CarInfo with id %s not found", id)));
  }
}
