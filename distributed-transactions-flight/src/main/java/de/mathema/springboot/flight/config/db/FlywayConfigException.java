package de.mathema.springboot.flight.config.db;

public class FlywayConfigException extends RuntimeException {

  public FlywayConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
