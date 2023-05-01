package de.mathema.springboot.car.migration;

import de.mathema.springboot.car.repository.CarInfo;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1_05_0__createCarInfos extends BaseJavaMigration {

  @Override
  public void migrate(Context context) {
    FlywayMigrationUtils.saveCarInfo(context, createCarInfo("CAR-INFO-1", "Audi A3", 6));
    FlywayMigrationUtils.saveCarInfo(context, createCarInfo("CAR-INFO-2", "VW Multivan", 0));
    FlywayMigrationUtils.saveCarInfo(context, createCarInfo("CAR-INFO-3", "BMW X5", 4));
  }

  CarInfo createCarInfo(final String id, final String description, final int quantity) {
    final CarInfo carInfo = CarInfo.builder()
        .id(id)
        .description(description)
        .quantity(quantity)
        .build();
    carInfo.prePersist();
    return carInfo;
  }
}
