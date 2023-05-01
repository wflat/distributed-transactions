package de.mathema.springboot.travel.process.health;

import de.mathema.springboot.common.exception.BadGatewayException;

public class CamundaUnavailableException extends BadGatewayException {

  public CamundaUnavailableException(final String message) {
    super(message);
  }
}
