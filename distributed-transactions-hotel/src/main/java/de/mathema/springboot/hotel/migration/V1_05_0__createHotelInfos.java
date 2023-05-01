package de.mathema.springboot.hotel.migration;

import de.mathema.springboot.hotel.repository.HotelInfo;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1_05_0__createHotelInfos extends BaseJavaMigration {

  @Override
  public void migrate(Context context) {
    FlywayMigrationUtils.saveHotelInfo(context, createHotelInfo("HOTEL-INFO-1", "Leonardo Royal Hotel Nürnberg", 6));
    FlywayMigrationUtils.saveHotelInfo(context, createHotelInfo("HOTEL-INFO-2", "Park Inn by Radisson Nürnberg", 0));
    FlywayMigrationUtils.saveHotelInfo(context, createHotelInfo("HOTEL-INFO-3", "Novina Hotel Tillypark", 4));
  }

  HotelInfo createHotelInfo(final String id, final String description, final int quantity) {
    final HotelInfo hotelInfo = HotelInfo.builder()
        .id(id)
        .description(description)
        .quantity(quantity)
        .build();
    hotelInfo.prePersist();
    return hotelInfo;
  }
}
