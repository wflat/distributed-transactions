package de.mathema.springboot.flight.migration;

import de.mathema.springboot.flight.repository.FlightInfo;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1_05_0__createFlightInfos extends BaseJavaMigration {

  @Override
  public void migrate(Context context) {
    FlywayMigrationUtils.saveFlightInfo(context, createFlightInfo("FLIGHT-INFO-1", "LH 1797: Palma de Mallorca => München", 10));
    FlywayMigrationUtils.saveFlightInfo(context, createFlightInfo("FLIGHT-INFO-2", "EN 8287: Mailand => München", 2));
    FlywayMigrationUtils.saveFlightInfo(context, createFlightInfo("FLIGHT-INFO-3", "XC 7905: Antalya => Nürnberg", 4));
  }

  FlightInfo createFlightInfo(final String id, final String description, final int quantity) {
    final FlightInfo flightInfo = FlightInfo.builder()
        .id(id)
        .description(description)
        .quantity(quantity)
        .build();
    flightInfo.prePersist();
    return flightInfo;
  }
}
