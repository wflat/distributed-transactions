package de.mathema.springboot.car.migration;

import de.mathema.springboot.car.repository.CarInfo;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Slf4j
public final class FlywayMigrationUtils {

  private FlywayMigrationUtils() {
  }

  public static void saveCarInfo(final Context context, final CarInfo carInfo) {
    final String sqlStmt = "INSERT INTO car__info(id, description, created, updated, quantity, version)"
        + " VALUES (?, ?, ?, ?, ?, 0)";
    try (PreparedStatement preparedStatement = context.getConnection().prepareStatement(sqlStmt)) {
      preparedStatement.setString(1, carInfo.getId());
      preparedStatement.setString(2, carInfo.getDescription());
      preparedStatement.setTimestamp(3, Timestamp.valueOf(carInfo.getCreated()));
      preparedStatement.setTimestamp(4, Timestamp.valueOf(carInfo.getUpdated()));
      preparedStatement.setInt(5, carInfo.getQuantity());
      preparedStatement.execute();
    } catch (final Exception e) {
      final FlywayMigrationException exception =
          new FlywayMigrationException(String.format("Could not save CarInfo %s", carInfo.getId()), e);
      log.error(exception.getMessage(), e);
      throw exception;
    }
  }
}
