package de.mathema.springboot.travel.process.tasks;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription("TravelBookingProcessBookFlight")
public class TravelBookingProcessBookFlightHandler extends ExternalTaskAktionAdapter {

  @Autowired
  public TravelBookingProcessBookFlightHandler(final TravelBookingProcessBookFlightAction action) {
    super(action);
  }
}