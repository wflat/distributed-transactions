package de.mathema.springboot.common.jms;

import java.util.Objects;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;

import lombok.Getter;

public class JmsMessageSender {

  @Getter
  private final JmsTemplate jmsTemplate;
  @Getter
  private final String queueName;

  public JmsMessageSender(final JmsTemplate jmsTemplate, final String queueName) {
    Objects.requireNonNull(jmsTemplate);
    Objects.requireNonNull(queueName);

    this.jmsTemplate = jmsTemplate;
    this.queueName = queueName;
  }

  public Message convertAndSend(final Object objToSend) {
    return convertAndSend(objToSend, null);
  }

  public Message convertAndSend(final Object objToSend, final String correlationId) {
    final JmsMessageCreator jmsMessageCreator = new JmsMessageCreator(objToSend, correlationId, this.jmsTemplate.getMessageConverter());
    this.jmsTemplate.send(this.queueName, jmsMessageCreator);
    return jmsMessageCreator.getMessage();
  }

  @Getter
  private static class JmsMessageCreator implements MessageCreator {

    private final Object objToSend;
    private final MessageConverter messageConverter;
    private final String correlationId;
    private Message message;

    public JmsMessageCreator(final Object objToSend, final String correlationId, final MessageConverter messageConverter) {
      Objects.requireNonNull(objToSend);
      Objects.requireNonNull(messageConverter);

      this.objToSend = objToSend;
      this.correlationId = correlationId;
      this.messageConverter = messageConverter;
    }

    @Override
    public Message createMessage(final Session session) throws JMSException {
      this.message = messageConverter.toMessage(this.objToSend, session);
      if (StringUtils.isNotEmpty(this.correlationId)) {
        this.message.setJMSCorrelationID(this.correlationId);
      }
      return this.message;
    }

    public Message getMessage() {
      if (this.message == null) {
        throw new IllegalStateException("No JMS message available.");
      }
      return this.message;
    }
  }
}
