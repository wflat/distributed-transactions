package de.mathema.springboot.travel.service;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.travel.config.service.ServicesProperties;
import de.mathema.springboot.travel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
class SaveTravelBookingAction {

  private final TravelBookingRepository travelBookingRepository;
  private final TravelOutputRepository travelOutputRepository;
  private final ServicesProperties servicesProperties;
  private final JsonService jsonService;

  public de.mathema.springboot.travel.repository.TravelBooking save(final de.mathema.springboot.travel.repository.TravelBooking travelBooking) {
    final de.mathema.springboot.travel.repository.TravelBooking result = travelBookingRepository.save(init(travelBooking));
    if (servicesProperties.isUseEvents()) {
      createTravelOutput(result);
    }
    return result;
  }

  de.mathema.springboot.travel.repository.TravelBooking init(final de.mathema.springboot.travel.repository.TravelBooking travelBooking) {
    travelBooking.setStatus(TravelStatus.NEW);
    return travelBooking;
  }

  void createTravelOutput(final de.mathema.springboot.travel.repository.TravelBooking result) {
    // TODO: OutputEvent erzeugen damit die Verarbeitung startet
    final TravelOutput output = null;

//    log.info("Create OutputEvent: {}/{}/{}/{} for TravelBooking {}",
//        output.getEntityType(), entityEvent.getEventType(), entityEvent.getEntityId(), entityEvent.getEntityQuantity(),
//        entityEvent.getTravelBookingId());
    travelOutputRepository.save(output);
  }

  private TravelOutput createOutputEvent(de.mathema.springboot.travel.repository.TravelBooking result) {
    final TravelOutput output = TravelOutput.builder()
        .entityType(EntityType.HOTEL)
        .build();
    output.prePersist();
    final EntityEvent entityEvent = EntityEvent.builder()
        .eventId(output.getId())
        .eventType(EventType.BOOK)
        .travelBookingId(result.getId())
        .entityId(result.getHotelId())
        .entityQuantity(result.getHotelQuantity())
        .build();
    output.setEventJson(jsonService.json(entityEvent));
    return output;
  }
}
