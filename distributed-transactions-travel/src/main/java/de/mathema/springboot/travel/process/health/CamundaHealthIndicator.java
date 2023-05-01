package de.mathema.springboot.travel.process.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.mathema.springboot.common.health.ServiceHealthIndicator;
import de.mathema.springboot.travel.config.service.ServicesProperties;

@Component
public class CamundaHealthIndicator extends ServiceHealthIndicator {

  @Autowired
  public CamundaHealthIndicator(final RestTemplate restTemplate,
                                final ServicesProperties servicesProperties) {
    super(restTemplate, servicesProperties.getCamunda().getBaseUrl());
  }
}
