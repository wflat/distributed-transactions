package de.mathema.springboot.car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"de.mathema.springboot.car", "de.mathema.springboot.common.service"})
public class CarApplication {

  public static void main(String[] args) {
    SpringApplication.run(CarApplication.class, args);
  }
}
