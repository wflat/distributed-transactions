package de.mathema.springboot.travel.config.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicesProperties {

  private boolean useEvents;
  private ServiceInfo camunda;
  private ServiceInfo hotel;
  private ServiceInfo car;
  private ServiceInfo flight;

  @Getter
  @Setter
  public static class ServiceInfo {

    private String baseUrl;
    private String entityPath;
  }
}
