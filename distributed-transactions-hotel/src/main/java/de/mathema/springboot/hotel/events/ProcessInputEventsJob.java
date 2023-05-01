package de.mathema.springboot.hotel.events;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.hotel.repository.HotelInfo;
import de.mathema.springboot.hotel.repository.HotelInfoNotFoundException;
import de.mathema.springboot.hotel.repository.HotelInfoRepository;
import de.mathema.springboot.hotel.repository.HotelInputRepository;
import de.mathema.springboot.hotel.repository.HotelOutput;
import de.mathema.springboot.hotel.repository.HotelOutputRepository;
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

  private final HotelInfoRepository hotelInfoRepository;
  private final HotelInputRepository hotelInputRepository;
  private final HotelOutputRepository hotelOutputRepository;
  private final JsonService jsonService;

  @Scheduled(initialDelayString = "PT0S", fixedDelay = 100)
  @SchedulerLock(name = "ProcessInputEventsJob.process")
  public void process() {
    log.debug("Start processing ...");

    hotelInputRepository.getFirstEvent().ifPresent(inputEvent -> {
      log.info("Process InputEvent: {}/{}",
          inputEvent.getId(), inputEvent.getCreated().toString());

      final EntityEvent event = jsonService.objekt(inputEvent.getEventJson(), EntityEvent.class);
      log.info("Process EntityEvent: {}/{}/{}/{}",
          event.getEventId(), event.getEventType(), event.getEntityId(), event.getEntityQuantity());

      try {
        final HotelInfo hotelInfo = getHotelInfo(event.getEntityId());
        // TODO: inputEvent verarbeiten und HotelInfo modifizieren. OutputEvent schreiben
      } catch (final Exception exc) {
        log.error("Exception: {}", exc.getMessage());
        createFailureEvent(event, exc.getMessage());
      }
      inputEvent.setProcessed(true);
    });

    log.debug("End processing");
  }

  void handleUnexpectedEvent(final EntityEvent event) {
    log.warn("Unexpected HotelEvent received {}/{} for TravelBooking {}",
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

    final HotelOutput output = HotelOutput.builder()
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
    hotelOutputRepository.save(output);
  }

  HotelInfo getHotelInfo(final String id) {
    return hotelInfoRepository.findById(id)
        .orElseThrow(() -> new HotelInfoNotFoundException(String.format("HotelInfo with id %s not found", id)));
  }
}
