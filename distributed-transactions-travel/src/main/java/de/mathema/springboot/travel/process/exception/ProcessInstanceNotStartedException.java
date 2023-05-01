package de.mathema.springboot.travel.process.exception;


import de.mathema.springboot.common.exception.InternalServerErrorException;

public class ProcessInstanceNotStartedException extends InternalServerErrorException {

  public ProcessInstanceNotStartedException(final String nachricht) {
    super(nachricht);
  }
}
