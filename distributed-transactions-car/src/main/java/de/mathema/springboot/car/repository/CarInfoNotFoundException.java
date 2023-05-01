package de.mathema.springboot.car.repository;

import de.mathema.springboot.common.exception.NotFoundException;

public class CarInfoNotFoundException extends NotFoundException {

  public CarInfoNotFoundException(final String message) {
    super(message);
  }
}
