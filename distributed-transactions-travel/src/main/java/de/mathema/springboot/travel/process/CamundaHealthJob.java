package de.mathema.springboot.travel.process;

import de.mathema.springboot.travel.process.health.CamundaHealthIndicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "travel.service", name = "use-events", havingValue = "false")
@Profile("!test")
public class CamundaHealthJob {

  private volatile boolean interrupted = false;

  private final CamundaHealthIndicator processengineHealthIndicator;
  private final ExternalTaskClient externalTaskClient;

  @PreDestroy
  public void preDestroy() {
    log.debug("CamundaHealthJob.preDestroy(.)");
    interrupted = true;

    if (externalTaskClient.isActive()) {
      log.info("Travel service will shut down. Stop ExternalTask processing ...");
      externalTaskClient.stop();
      log.info("Stopped");
    }
  }

  @Scheduled(initialDelayString = "PT0S", fixedDelayString = "PT60S")
  public void pruefeHealthStatus() {
    if (interrupted) {
      return;
    }

    if (processengineHealthIndicator.isAvailable()) {
      if (!externalTaskClient.isActive()) {
        log.info("Camunda available. Start ExternalTask processing ...");
        externalTaskClient.start();
        log.info("Started");
      }
    } else {
      if (externalTaskClient.isActive()) {
        log.warn("Camunda unavailable. Stop ExternalTask processing ...");
        externalTaskClient.stop();
        log.warn("Stopped");
      }
    }
  }
}
