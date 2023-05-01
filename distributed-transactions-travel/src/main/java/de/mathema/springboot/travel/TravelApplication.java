package de.mathema.springboot.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"de.mathema.springboot.travel", "de.mathema.springboot.common.service"})
public class TravelApplication {

  public static void main(String[] args) {
    SpringApplication.run(TravelApplication.class, args);
  }
}
