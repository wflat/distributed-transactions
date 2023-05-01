package de.mathema.springboot.travel.process.tasks;

import de.mathema.springboot.travel.process.ProcessVariable;
import de.mathema.springboot.travel.process.exception.BpmnErrorException;
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

@Transactional(dontRollbackOn = {BpmnErrorException.class})
@Component
@RequiredArgsConstructor
@Slf4j
public class TravelBookingProcessBookFlightAction implements ExternalTaskAction {

  private final TravelBookingRepository travelBookingRepository;
  private final HttpBookingService httpBookingService;

  @Override
  public VariableMap verarbeite(final ExternalTask externalTask) {
    final String travelBookingId = externalTask.getVariable(ProcessVariable.TRAVEL_BOOKING_ID.getVariablenname());
    final TravelBooking travelBooking = travelBookingRepository.getTravelBooking(travelBookingId);

    // TODO: httpBookingService.patchFlightEntity() mit richtigem Event aufrufen und den Response verarbeiten
    // TODO: Was passiert bei einem Fehler? Um Kompensation kümmern!

    return Variables.createVariables();
  }
}
