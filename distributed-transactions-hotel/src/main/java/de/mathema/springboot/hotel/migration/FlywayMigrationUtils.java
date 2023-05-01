package de.mathema.springboot.hotel.migration;

import de.mathema.springboot.hotel.repository.HotelInfo;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Slf4j
public final class FlywayMigrationUtils {

  private FlywayMigrationUtils() {
  }

  public static void saveHotelInfo(final Context context, final HotelInfo hotelInfo) {
    final String sqlStmt = "INSERT INTO hotel__info(id, description, created, updated, quantity, version)"
        + " VALUES (?, ?, ?, ?, ?, 0)";
    try (PreparedStatement preparedStatement = context.getConnection().prepareStatement(sqlStmt)) {
      preparedStatement.setString(1, hotelInfo.getId());
      preparedStatement.setString(2, hotelInfo.getDescription());
      preparedStatement.setTimestamp(3, Timestamp.valueOf(hotelInfo.getCreated()));
      preparedStatement.setTimestamp(4, Timestamp.valueOf(hotelInfo.getUpdated()));
      preparedStatement.setInt(5, hotelInfo.getQuantity());
      preparedStatement.execute();
    } catch (final Exception e) {
      final FlywayMigrationException exception =
          new FlywayMigrationException(String.format("Could not save HotelInfo %s", hotelInfo.getId()), e);
      log.error(exception.getMessage(), e);
      throw exception;
    }
  }
}
