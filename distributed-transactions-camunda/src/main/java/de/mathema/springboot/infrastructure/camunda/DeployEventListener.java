package de.mathema.springboot.infrastructure.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeployEventListener {

  private final ProcessInstancesMigration processInstancesMigration;

  @EventListener
  public void onPostDeploy(final PostDeployEvent event) {
    log.info(String.format("%s received in %s", ClassUtils.getSimpleName(event),
        ClassUtils.getSimpleName(this)));

    processInstancesMigration.migrate(event.getProcessEngine());
  }

  @EventListener
  public void onPreUndeploy(final PreUndeployEvent event) {
    log.info(String.format("%s received in %s", ClassUtils.getSimpleName(event),
        ClassUtils.getSimpleName(this)));
  }
}
