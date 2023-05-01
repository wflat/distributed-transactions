package de.mathema.springboot.common.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ServiceHealthIndicator extends AbstractHealthIndicator {

  private final RestTemplate restTemplate;
  private final String baseUrl;

  public boolean isAvailable() {
    final String healthUrl = baseUrl + "/actuator/health";
    try {
      final ResponseEntity<String> result = restTemplate.getForEntity(healthUrl, String.class);
      return result.getStatusCode().is2xxSuccessful();
    } catch (final Exception e) {
      log.warn("Exception during the call of {} : {}", healthUrl, e.getMessage());
      return false;
    }
  }

  @Override
  protected void doHealthCheck(Health.Builder builder) {
    builder.status(isAvailable() ? Status.UP : Status.DOWN);
  }
}
