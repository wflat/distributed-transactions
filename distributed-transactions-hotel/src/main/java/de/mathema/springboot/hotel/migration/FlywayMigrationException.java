package de.mathema.springboot.hotel.migration;

public class FlywayMigrationException extends RuntimeException {

  public FlywayMigrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
