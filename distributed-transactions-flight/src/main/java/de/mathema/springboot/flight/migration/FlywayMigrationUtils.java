package de.mathema.springboot.flight.migration;

import de.mathema.springboot.flight.repository.FlightInfo;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Slf4j
public final class FlywayMigrationUtils {

  private FlywayMigrationUtils() {
  }

  public static void saveFlightInfo(final Context context, final FlightInfo flightInfo) {
    final String sqlStmt = "INSERT INTO flight__info(id, description, created, updated, quantity, version)"
        + " VALUES (?, ?, ?, ?, ?, 0)";
    try (PreparedStatement preparedStatement = context.getConnection().prepareStatement(sqlStmt)) {
      preparedStatement.setString(1, flightInfo.getId());
      preparedStatement.setString(2, flightInfo.getDescription());
      preparedStatement.setTimestamp(3, Timestamp.valueOf(flightInfo.getCreated()));
      preparedStatement.setTimestamp(4, Timestamp.valueOf(flightInfo.getUpdated()));
      preparedStatement.setInt(5, flightInfo.getQuantity());
      preparedStatement.execute();
    } catch (final Exception e) {
      final FlywayMigrationException exception =
          new FlywayMigrationException(String.format("Could not save FlightInfo %s", flightInfo.getId()), e);
      log.error(exception.getMessage(), e);
      throw exception;
    }
  }
}
