package de.mathema.springboot.flight.migration;

public class FlywayMigrationException extends RuntimeException {

  public FlywayMigrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
