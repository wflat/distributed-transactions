package de.mathema.springboot.travel.process.tasks;

import de.mathema.springboot.travel.process.ProcessVariable;
import de.mathema.springboot.travel.repository.TravelBooking;
import de.mathema.springboot.travel.repository.TravelBookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class TravelBookingProcessInitTaskAction implements ExternalTaskAction {

  private final TravelBookingRepository travelBookingRepository;

  @Override
  public VariableMap verarbeite(ExternalTask externalTask) {
    final String travelBookingId = externalTask.getVariable(ProcessVariable.TRAVEL_BOOKING_ID.getVariablenname());
    final TravelBooking travelBooking = travelBookingRepository.getTravelBooking(travelBookingId);

    travelBooking.setProcessInstanceId(externalTask.getProcessInstanceId());

    return Variables.createVariables()
        .putValue(ProcessVariable.CREATED.getVariablenname(), Timestamp.valueOf(travelBooking.getCreated()));
  }
}
