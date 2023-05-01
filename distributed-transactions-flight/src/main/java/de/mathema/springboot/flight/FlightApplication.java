package de.mathema.springboot.flight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"de.mathema.springboot.flight", "de.mathema.springboot.common.service"})
public class FlightApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlightApplication.class, args);
  }
}
