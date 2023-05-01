package de.mathema.springboot.travel.config.activemq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveMqProperties {

  private String brokerUrl;
  private ServiceQueues hotel;
  private ServiceQueues car;
  private ServiceQueues flight;

  @Getter
  @Setter
  public static class ServiceQueues {
    private String inputQueue;
    private String outputQueue;
  }
}
