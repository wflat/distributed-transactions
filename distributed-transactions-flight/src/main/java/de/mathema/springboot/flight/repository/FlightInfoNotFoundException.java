package de.mathema.springboot.flight.repository;

import de.mathema.springboot.common.exception.NotFoundException;

public class FlightInfoNotFoundException extends NotFoundException {

  public FlightInfoNotFoundException(final String message) {
    super(message);
  }
}
