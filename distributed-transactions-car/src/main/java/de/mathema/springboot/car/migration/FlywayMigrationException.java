package de.mathema.springboot.car.migration;

public class FlywayMigrationException extends RuntimeException {

  public FlywayMigrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
