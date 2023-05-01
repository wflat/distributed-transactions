package de.mathema.springboot.flight;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.flight.service.FlightInfo;
import de.mathema.springboot.flight.service.FlightInfoService;
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
public class FlightInfoEndpoints {

  private final FlightInfoService flightInfoService;

  @GetMapping(path = "/flight-infos", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<List<FlightInfo>> getAll() {
    return ResponseEntity.ok(flightInfoService.getAll());
  }

  @PostMapping(path = "/flight-infos", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<FlightInfo> saveFlightInfo(@RequestBody final FlightInfo flightInfo) {
    return ResponseEntity.status(HttpStatus.CREATED).body(flightInfoService.save(flightInfo));
  }

  @GetMapping(path = "/flight-infos/{flight-info-id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<FlightInfo> getById(@PathVariable("flight-info-id") final String flightInfoId) {
    return ResponseEntity.ok(flightInfoService.getById(flightInfoId));
  }

  @PatchMapping(path = "/flight-infos/{flight-info-id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<FlightInfo> updateById(@PathVariable("flight-info-id") final String flightInfoId,
                                               @RequestBody final FlightInfo flightInfo) {
    return ResponseEntity.ok(flightInfoService.update(flightInfoId, flightInfo));
  }

  @PatchMapping(path = "/flight-infos/{flight-info-id}/quantity", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<EntityEvent> updateQuantity(@PathVariable("flight-info-id") final String flightInfoId,
                                                    @RequestBody final EntityEvent entityEvent) {
    return ResponseEntity.ok(flightInfoService.updateQuantity(flightInfoId, entityEvent));
  }
}
