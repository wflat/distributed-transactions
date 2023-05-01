package de.mathema.springboot.infrastructure.db;

import de.mathema.springboot.common.db.DatabaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class FlywayConfig {

  @Bean(initMethod = "migrate")
  public Flyway camundaFlyway(final DataSource dataSource) {
    try {
      final String vendor = DatabaseUtils.getDbVendor(dataSource);

      return Flyway.configure()
          .dataSource(dataSource)
          .table("flyway_camunda_schema_history")
          .locations("classpath:db/migration/default", "classpath:db/migration/" + vendor)
          .baselineOnMigrate(true)
          .baselineVersion(MigrationVersion.fromVersion("0_00_0"))
          .ignoreMigrationPatterns("*:missing")
          .load();
    } catch (Exception e) {
      log.error("Exception in FlywayConfig", e);
      throw new FlywayConfigException("Exception in FlywayConfig", e);
    }
  }
}
