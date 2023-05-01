package de.mathema.springboot.travel.process.tasks;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import de.mathema.springboot.travel.process.ProcessVariable;
import de.mathema.springboot.travel.process.exception.BpmnErrorException;
import de.mathema.springboot.travel.repository.TravelBooking;
import de.mathema.springboot.travel.repository.TravelBookingRepository;
import de.mathema.springboot.travel.repository.TravelStatus;
import de.mathema.springboot.travel.service.HttpBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Transactional(dontRollbackOn = {BpmnErrorException.class})
@Component
@RequiredArgsConstructor
@Slf4j
public class TravelBookingProcessBookCarAction implements ExternalTaskAction {

  private final TravelBookingRepository travelBookingRepository;
  private final HttpBookingService httpBookingService;

  @Override
  public VariableMap verarbeite(final ExternalTask externalTask) {
    final String travelBookingId = externalTask.getVariable(ProcessVariable.TRAVEL_BOOKING_ID.getVariablenname());
    final TravelBooking travelBooking = travelBookingRepository.getTravelBooking(travelBookingId);

    final EntityEvent response = httpBookingService.patchCarEntity(EntityEvent.builder()
        .eventType(EventType.BOOK)
        .travelBookingId(travelBooking.getId())
        .entityId(travelBooking.getCarId())
        .entityQuantity(travelBooking.getCarQuantity())
        .build());

    if (EventType.CONFIRMATION != response.getEventType()) {
      travelBooking.setStatus(TravelStatus.FAILED);
      throw new BpmnErrorException(String.format("Car booking failed for TravelBooking %s : %s/%s/%s",
          travelBooking.getId(), response.getEventType(), response.getEntityId(), response.getMessage()));
    }

    log.info("TravelBooking {}: car {}/{} booked",
        travelBooking.getId(), travelBooking.getCarId(), travelBooking.getCarQuantity());
    travelBooking.setCarApproved(true);
    return Variables.createVariables();
  }
}
