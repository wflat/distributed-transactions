package de.mathema.springboot.infrastructure.db;

public class FlywayConfigException extends RuntimeException {

  public FlywayConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
