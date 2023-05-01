package de.mathema.springboot.travel.process.tasks;

import de.mathema.springboot.travel.process.exception.BpmnErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@RequiredArgsConstructor
@Slf4j
public class ExternalTaskAktionAdapter implements ExternalTaskHandler {

  static final String ERROR_CODE_BOOKING_FAILED = "Error-Booking-Failed";
  static final long TIMEOUT_IN_MS = 5_000L;
  static final int MAX_RETRIES = 5;

  private final ExternalTaskAction externalTaskAction;

  @Override
  public void execute(final ExternalTask externalTask,
                      final ExternalTaskService externalTaskService) {
    try {
      final VariableMap variablen = externalTaskAction.verarbeite(externalTask);

      externalTaskService.complete(externalTask, variablen);
    } catch (final BpmnErrorException e) {
      log.info("{}: {}/{}", this.getClass().getSimpleName(), e.getClass().getSimpleName(), e.getMessage());

      externalTaskService.handleBpmnError(externalTask, ERROR_CODE_BOOKING_FAILED, e.getMessage());
    } catch (final Exception e) {
      if (e instanceof ObjectOptimisticLockingFailureException) {
        log.info("{}: {}", this.getClass().getSimpleName(), e.getMessage());
      } else {
        log.warn("{}: {}", this.getClass().getSimpleName(), e.getMessage());
      }

      final int retries = getRetries(externalTask);
      final long pause = getTimeout(retries);
      externalTaskService.handleFailure(externalTask, e.getMessage(),
          ExceptionUtils.getStackTrace(e), retries, pause);
    }
  }

  private int getRetries(final ExternalTask externalTask) {
    final Integer retries = externalTask.getRetries();
    return retries == null ? MAX_RETRIES : retries - 1;
  }

  private Long getTimeout(final int retries) {
    return TIMEOUT_IN_MS * (MAX_RETRIES - retries);
  }
}
