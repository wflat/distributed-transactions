package de.mathema.springboot.travel.repository;

import de.mathema.springboot.common.exception.NotFoundException;

public class TravelBookingNotFoundException extends NotFoundException {

  public TravelBookingNotFoundException(final String message) {
    super(message);
  }
}
