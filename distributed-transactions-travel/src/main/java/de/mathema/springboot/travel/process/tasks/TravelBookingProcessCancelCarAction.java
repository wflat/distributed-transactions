package de.mathema.springboot.travel.process.tasks;

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
public class TravelBookingProcessCancelCarAction implements ExternalTaskAction {

  private final TravelBookingRepository travelBookingRepository;
  private final HttpBookingService httpBookingService;

  @Override
  public VariableMap verarbeite(final ExternalTask externalTask) {
    final String travelBookingId = externalTask.getVariable(ProcessVariable.TRAVEL_BOOKING_ID.getVariablenname());
    final TravelBooking travelBooking = travelBookingRepository.getTravelBooking(travelBookingId);

    // TODO: httpBookingService.patchCarEntity() mit richtigem Event aufrufen und den Response verarbeiten

    return Variables.createVariables();
  }
}
