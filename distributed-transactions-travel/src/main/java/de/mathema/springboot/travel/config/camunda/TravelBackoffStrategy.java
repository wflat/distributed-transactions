package de.mathema.springboot.travel.config.camunda;

import java.util.List;

import org.camunda.bpm.client.backoff.ErrorAwareBackoffStrategy;
import org.camunda.bpm.client.exception.ExternalTaskClientException;
import org.camunda.bpm.client.task.ExternalTask;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class TravelBackoffStrategy implements ErrorAwareBackoffStrategy {

  protected int level = 0;

  @Override
  public void reconfigure(final List<ExternalTask> externalTasks, final ExternalTaskClientException exception) {
    if (exception != null) {
      level++;
    } else {
      level = 0;
    }
  }

  @Override
  public long calculateBackoffTime() {
    if (level == 0) {
      return 0L;
    }

    final long backoffTime = (long) (500L * Math.pow(2d, level - 1d));
    final long result = Math.min(backoffTime, 60000L);
    log.info("ExternalTaskClient : Warte nach {} Fehlern {} millis", level, result);
    return result;
  }
}
