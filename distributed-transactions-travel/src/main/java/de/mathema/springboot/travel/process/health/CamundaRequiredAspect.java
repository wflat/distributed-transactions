package de.mathema.springboot.travel.process.health;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "travel.service", name = "use-events", havingValue = "false")
public class CamundaRequiredAspect {

  private final CamundaHealthIndicator camundaHealthIndicator;

  @Around("@annotation(de.mathema.springboot.travel.process.health.CamundaRequired)")
  public Object checkCamundaAvailability(final ProceedingJoinPoint proceedingJoinPoint)
      throws Throwable {
    if (!camundaHealthIndicator.isAvailable()) {
      throw new CamundaUnavailableException("Camunda is temporarily unavailable");
    }

    return proceedingJoinPoint.proceed();
  }
}
