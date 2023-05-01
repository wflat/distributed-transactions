package de.mathema.springboot.travel.process.health;

import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExternalTaskClientHealthIndicator extends AbstractHealthIndicator {

  private final ExternalTaskClient externalTaskClient;

  public boolean isActive() {
    try {
      return externalTaskClient.isActive();
    } catch (final Exception e) {
      return false;
    }
  }

  @Override
  protected void doHealthCheck(Health.Builder builder) {
    builder.status(isActive() ? Status.UP : Status.DOWN);
  }
}
