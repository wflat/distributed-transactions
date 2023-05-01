package de.mathema.springboot.travel.events;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.travel.repository.*;
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

  private final TravelBookingRepository travelBookingRepository;
  private final TravelInputRepository travelInputRepository;
  private final TravelOutputRepository travelOutputRepository;
  private final JsonService jsonService;

  @Scheduled(initialDelayString = "PT0S", fixedDelay = 100)
  @SchedulerLock(name = "ProcessInputEventsJob.process")
  public void process() {
    log.debug("Start processing ...");

    travelInputRepository.getFirstEvent().ifPresent(inputEvent -> {
      log.info("Process InputEvent: {}/{}/{}",
          inputEvent.getId(), inputEvent.getEntityType(), inputEvent.getCreated().toString());

      final EntityEvent event = jsonService.objekt(inputEvent.getEventJson(), EntityEvent.class);
      final TravelBooking travelBooking = travelBookingRepository.getTravelBooking(event.getTravelBookingId());
      log.info("Process EntityEvent: {}/{}/{}/{}",
          event.getEventId(), event.getEventType(), event.getEntityId(), event.getMessage());

      // TODO: inputEvent verarbeiten und CarInfo travelBooking. OutputEvent schreiben
      inputEvent.setProcessed(true);
    });

    log.debug("End processing");
  }

  void sendCancelEvents(final TravelBooking travelBooking) {
    if (travelBooking.isHotelApproved()) {
      createTravelOutput(EntityType.HOTEL, EventType.CANCEL,
          travelBooking.getId(), travelBooking.getHotelId(), travelBooking.getHotelQuantity());
    }
    if (travelBooking.isCarApproved()) {
      createTravelOutput(EntityType.CAR, EventType.CANCEL,
          travelBooking.getId(), travelBooking.getCarId(), travelBooking.getCarQuantity());
    }
    if (travelBooking.isFlightApproved()) {
      createTravelOutput(EntityType.FLIGHT, EventType.CANCEL,
          travelBooking.getId(), travelBooking.getFlightId(), travelBooking.getFlightQuantity());
    }
  }

  void createTravelOutput(final EntityType entityType, final EventType eventType,
                          final String travelBookingId,
                          final String entityId, final int entityQuantity) {
    log.info("Create OutputEvent: {}/{}/{}/{} for TravelBooking {}",
        entityType, eventType, entityId, entityQuantity, travelBookingId);

    final TravelOutput output = TravelOutput.builder()
        .entityType(entityType)
        .build();
    output.prePersist();
    final EntityEvent entityEvent = EntityEvent.builder()
        .eventId(output.getId())
        .eventType(eventType)
        .travelBookingId(travelBookingId)
        .entityId(entityId)
        .entityQuantity(entityQuantity)
        .build();
    output.setEventJson(jsonService.json(entityEvent));
    travelOutputRepository.save(output);
  }
}
