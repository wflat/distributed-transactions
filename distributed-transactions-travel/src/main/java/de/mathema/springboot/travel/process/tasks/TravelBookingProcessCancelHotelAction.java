package de.mathema.springboot.travel.process.tasks;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import de.mathema.springboot.travel.process.ProcessVariable;
import de.mathema.springboot.travel.repository.TravelBooking;
import de.mathema.springboot.travel.repository.TravelBookingRepository;
import de.mathema.springboot.travel.service.HttpBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class TravelBookingProcessCancelHotelAction implements ExternalTaskAction {

  private final TravelBookingRepository travelBookingRepository;
  private final HttpBookingService httpBookingService;

  @Override
  public VariableMap verarbeite(final ExternalTask externalTask) {
    final String travelBookingId = externalTask.getVariable(ProcessVariable.TRAVEL_BOOKING_ID.getVariablenname());
    final TravelBooking travelBooking = travelBookingRepository.getTravelBooking(travelBookingId);

    final EntityEvent response = httpBookingService.patchHotelEntity(EntityEvent.builder()
        .eventType(EventType.CANCEL)
        .travelBookingId(travelBooking.getId())
        .entityId(travelBooking.getHotelId())
        .entityQuantity(travelBooking.getHotelQuantity())
        .build());

    if (EventType.CANCEL_CONFIRMATION == response.getEventType()) {
      log.info("TravelBooking {}: hotel {}/{} canceled",
          travelBooking.getId(), travelBooking.getHotelId(), travelBooking.getHotelQuantity());
    }
    travelBooking.setHotelApproved(false);
    return Variables.createVariables();
  }
}
