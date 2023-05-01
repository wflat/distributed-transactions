package de.mathema.springboot.infrastructure.activemq;

import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ActiveMqConfig {

  @Bean
  public BrokerService brokerService() throws Exception {
    final BrokerService broker = new BrokerService();
    broker.setBrokerName("distributed-transactions");
    broker.setUseShutdownHook(false);
    broker.setPersistent(false);
    broker.addConnector("tcp://localhost:61616");
    return broker;
  }
}
