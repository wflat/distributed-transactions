package de.mathema.springboot.hotel.config.activemq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveMqProperties {

  private String brokerUrl;
  private String inputQueue;
  private String outputQueue;
}
