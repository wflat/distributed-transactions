package de.mathema.springboot.hotel.repository;

import de.mathema.springboot.common.exception.NotFoundException;

public class HotelInfoNotFoundException extends NotFoundException {

  public HotelInfoNotFoundException(final String message) {
    super(message);
  }
}
