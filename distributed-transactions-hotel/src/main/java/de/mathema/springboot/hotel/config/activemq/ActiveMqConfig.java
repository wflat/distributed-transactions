package de.mathema.springboot.hotel.config.activemq;

import de.mathema.springboot.common.jms.JmsMessageSender;
import de.mathema.springboot.common.service.JsonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
@Slf4j
public class ActiveMqConfig {

  @Bean
  @ConfigurationProperties(prefix = "hotel.activemq")
  public ActiveMqProperties gpmProperties() {
    return new ActiveMqProperties();
  }

  @Bean
  public JmsMessageSender hotelJmsMessageSender(final JmsTemplate jmsTemplate,
                                                final ActiveMqProperties activeMqProperties) {
    return new JmsMessageSender(jmsTemplate, activeMqProperties.getOutputQueue());
  }

  @Bean
  public JmsTemplate jmsTemplate(final ActiveMQConnectionFactory jmsConnectionFactory,
                                 final MappingJackson2MessageConverter messageConverter) {
    final JmsTemplate jmsTemplate = new JmsTemplate(jmsConnectionFactory);
    jmsTemplate.setMessageConverter(messageConverter);
    jmsTemplate.setSessionTransacted(false);
    return jmsTemplate;
  }

  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
      final DefaultJmsListenerContainerFactoryConfigurer configurer,
      final ActiveMQConnectionFactory jmsConnectionFactory,
      final MessageConverter messageConverter) {
    final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    configurer.configure(factory, jmsConnectionFactory);
    factory.setErrorHandler(t -> log.error("DefaultJmsListenerContainerFactory - Exception was thrown: {}", t.getMessage()));
    factory.setMessageConverter(messageConverter);
    return factory;
  }

  @Bean
  public ActiveMQConnectionFactory jmsConnectionFactory(final ActiveMqProperties properties) {
    return new ActiveMQConnectionFactory(properties.getBrokerUrl());
  }

  @Bean
  public MappingJackson2MessageConverter messageConverter() {
    final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    converter.setObjectMapper(JsonService.createObjectMapper(false));
    return converter;
  }
}
