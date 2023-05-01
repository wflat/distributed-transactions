package de.mathema.springboot.flight.config.activemq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveMqProperties {

  private String brokerUrl;
  private String inputQueue;
  private String outputQueue;
}
