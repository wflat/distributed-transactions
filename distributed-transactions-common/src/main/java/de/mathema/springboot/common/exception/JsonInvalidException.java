package de.mathema.springboot.common.exception;

public class JsonInvalidException extends InternalServerErrorException {

  public JsonInvalidException(final String message, final Throwable grund) {
    super(message);
  }
}
