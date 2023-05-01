package de.mathema.springboot.travel;

import de.mathema.springboot.travel.service.TravelBookingService;
import de.mathema.springboot.travel.service.TravelBooking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class TravelBookingEndpoints {

  private final TravelBookingService travelBookingService;

  @GetMapping(path = "/travel-bookings", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<List<TravelBooking>> getAll() {
    return ResponseEntity.ok(travelBookingService.getAll());
  }

  @PostMapping(path = "/travel-bookings", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<TravelBooking> saveTravelBooking(@RequestBody final TravelBooking travelBooking) {
    return ResponseEntity.status(HttpStatus.CREATED).body(travelBookingService.save(travelBooking));
  }

  @GetMapping(path = "/travel-bookings/{travel-booking-id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<TravelBooking> getById(@PathVariable("travel-booking-id") String travelBookingId) {
    return ResponseEntity.ok(travelBookingService.getById(travelBookingId));
  }
}
