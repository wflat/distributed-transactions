package de.mathema.springboot.travel.process.tasks;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@ExternalTaskSubscription("TravelBookingProcessBookCar")
@Slf4j
public class TravelBookingProcessBookCarHandler extends ExternalTaskAktionAdapter {

  @Autowired
  public TravelBookingProcessBookCarHandler(final TravelBookingProcessBookCarAction action) {
    super(action);
  }
}
