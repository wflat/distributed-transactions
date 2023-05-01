package de.mathema.springboot.infrastructure;

import de.mathema.springboot.infrastructure.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.UUID;

import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.init;
import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.reset;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@SpringBootTest
@Slf4j
class TravelBookingProcessTest {

  static final String PROCESS_MODEL_PATH = "processes/travel-booking.bpmn";
  static final String PROCESS_ID = "Process_TravelBooking";
  static final String ACTIVITY_INIT_PROCESS = "Activity_InitProcess";
  static final String ACTIVITY_BOOK_HOTEL = "Activity_BookHotel";
  static final String ACTIVITY_BOOK_CAR = "Activity_BookCar";
  static final String ACTIVITY_BOOK_FLIGHT = "Activity_BookFlight";
  static final String ACTIVITY_CANCEL_HOTEL = "Activity_CancelHotel";
  static final String ACTIVITY_CANCEL_CAR = "Activity_CancelCar";
  static final String ACTIVITY_CANCEL_FLIGHT = "Activity_CancelFlight";
  static final String TRAVEL_BOOKING_ID = "travelBookingId";
  static final String EVENT_DO_COMPENSATION = "Event_DoCompensation";
  static final String TOPIC_TRAVEL_BOOKING_PROCESS_BOOK_CAR = "TravelBookingProcessBookCar";
  static final String TOPIC_TRAVEL_BOOKING_PROCESS_BOOK_FLIGHT = "TravelBookingProcessBookFlight";
  static final String TOPIC_TRAVEL_BOOKING_PROCESS_CANCEL_HOTEL = "TravelBookingProcessCancelHotel";
  static final String TOPIC_TRAVEL_BOOKING_PROCESS_CANCEL_CAR = "TravelBookingProcessCancelCar";
  static final String ANONYMOUS_WORKER = "anonymousWorker";
  static final String ERROR_CODE_BOOKING_FAILED = "Error-Booking-Failed";

  @Autowired
  ProcessEngine processEngine;

  @BeforeEach
  void setUp() {
    init(processEngine);
  }

  @AfterEach
  void tearDown() {
    ProcessUtils.removeProcesses(processEngine, PROCESS_ID);
    reset();
  }

  @Test
  @Deployment(resources = PROCESS_MODEL_PATH)
  void checkProcessDeployment() {
    Assertions.assertThat(processEngine.getRuntimeService()).isNotNull();
    Assertions.assertThat(processEngine.getTaskService()).isNotNull();
  }

  @Test
  @Deployment(resources = PROCESS_MODEL_PATH)
  void happyPath() {
    String travelBookingId = "1L";
    ProcessInstance processInstance = startProcessInstance(travelBookingId);
    assertProcessInstanceStart(travelBookingId, processInstance);

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_INIT_PROCESS);
    complete(externalTask(processInstance));

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_BOOK_HOTEL);
    complete(externalTask(processInstance));

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_BOOK_CAR);
    complete(externalTask(processInstance));

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_BOOK_FLIGHT);
    complete(externalTask(processInstance));

    assertThat(processInstanceQuery().processInstanceId(processInstance.getProcessInstanceId())
        .singleResult()).isNull();
  }

  @Test
  @Deployment(resources = PROCESS_MODEL_PATH)
  void failureInBookCar() {
    String travelBookingId = "1L";
    ProcessInstance processInstance = startProcessInstance(travelBookingId);
    assertProcessInstanceStart(travelBookingId, processInstance);

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_INIT_PROCESS);
    complete(externalTask(processInstance));

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_BOOK_HOTEL);
    complete(externalTask(processInstance));

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_BOOK_CAR);
    LockedExternalTask lockedTask = processEngine.getExternalTaskService()
        .fetchAndLock(1, ANONYMOUS_WORKER)
        .topic(TOPIC_TRAVEL_BOOKING_PROCESS_BOOK_CAR, 30L * 1000L)
        .execute().get(0);
    processEngine.getExternalTaskService().handleBpmnError(lockedTask.getId(), lockedTask.getWorkerId(),
        ERROR_CODE_BOOKING_FAILED);

    assertThat(processInstance).
        isNotWaitingAt(ACTIVITY_BOOK_HOTEL, ACTIVITY_BOOK_CAR, ACTIVITY_BOOK_FLIGHT,
            ACTIVITY_CANCEL_CAR, ACTIVITY_CANCEL_FLIGHT);
    assertThat(processInstance).isWaitingAt(ACTIVITY_CANCEL_HOTEL, EVENT_DO_COMPENSATION);

    complete(externalTask(processInstance));

    assertThat(processInstanceQuery().processInstanceId(processInstance.getProcessInstanceId())
        .singleResult()).isNull();
  }

  @Test
  @Deployment(resources = PROCESS_MODEL_PATH)
  void failureInBookFlight() {
    String travelBookingId = "1L";
    ProcessInstance processInstance = startProcessInstance(travelBookingId);
    assertProcessInstanceStart(travelBookingId, processInstance);

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_INIT_PROCESS);
    complete(externalTask(processInstance));

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_BOOK_HOTEL);
    complete(externalTask(processInstance));

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_BOOK_CAR);
    complete(externalTask(processInstance));

    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_BOOK_FLIGHT);
    LockedExternalTask lockedTask = lockExternalTask(TOPIC_TRAVEL_BOOKING_PROCESS_BOOK_FLIGHT);
    processEngine.getExternalTaskService().handleBpmnError(lockedTask.getId(), lockedTask.getWorkerId(),
        ERROR_CODE_BOOKING_FAILED);

    assertThat(processInstance).
        isNotWaitingAt(ACTIVITY_BOOK_HOTEL, ACTIVITY_BOOK_CAR, ACTIVITY_BOOK_FLIGHT, ACTIVITY_CANCEL_FLIGHT);
    assertThat(processInstance).isWaitingAt(ACTIVITY_CANCEL_HOTEL, ACTIVITY_CANCEL_CAR, EVENT_DO_COMPENSATION);

    complete(lockExternalTask(TOPIC_TRAVEL_BOOKING_PROCESS_CANCEL_HOTEL));
    complete(lockExternalTask(TOPIC_TRAVEL_BOOKING_PROCESS_CANCEL_CAR));

    assertThat(processInstanceQuery().processInstanceId(processInstance.getProcessInstanceId())
        .singleResult()).isNull();
  }

  LockedExternalTask lockExternalTask(final String topicName) {
    return processEngine.getExternalTaskService()
        .fetchAndLock(1, ANONYMOUS_WORKER)
        .topic(topicName, 30L * 1000L)
        .execute().get(0);
  }

  void assertProcessInstanceStart(String travelBookingId, ProcessInstance processInstance) {
    assertThat(processInstance).isNotNull();
    assertThat(processInstance).isStarted();
    assertThat(processInstance).isWaitingAtExactly(ACTIVITY_INIT_PROCESS);
    assertThat(processInstance).variables().hasFieldOrPropertyWithValue(TRAVEL_BOOKING_ID, travelBookingId);
  }

  ProcessInstance startProcessInstance(final String travelBookingId) {
    final ProcessInstance processInstance = processEngine.getRuntimeService()
        .startProcessInstanceByKey(PROCESS_ID,
            UUID.randomUUID().toString(), Map.of(TRAVEL_BOOKING_ID, travelBookingId));
    log.info("Started ProcessInstance {} for TravelBooking {}", processInstance.getProcessInstanceId(), travelBookingId);
    return processInstance;
  }
}
