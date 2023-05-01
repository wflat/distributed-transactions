package de.mathema.springboot.travel.process.exception;

public class BpmnErrorException extends RuntimeException {

  public BpmnErrorException(final String message) {
    super(message);
  }
}
