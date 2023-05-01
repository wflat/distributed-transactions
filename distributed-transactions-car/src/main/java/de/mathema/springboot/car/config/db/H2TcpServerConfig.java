package de.mathema.springboot.car.config.db;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;


@Configuration
@Slf4j
@Profile("!test")
public class H2TcpServerConfig {

  @Value("${h2.server.tcp-port:9140}")
  private int tcpPort;

  @Bean(initMethod = "start", destroyMethod = "stop")
  public Server h2Server() throws SQLException {
    log.info("Starte H2-TCP-Server auf Port {}", tcpPort);
    // DB-URL ist: jdbc:h2:tcp://localhost:9140/mem:carservice
    return Server.createTcpServer("-tcp", "-tcpAllowOthers",
        "-tcpPort", String.valueOf(tcpPort),
        "-tcpPassword", "sa");
  }
}
