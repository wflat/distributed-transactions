package de.mathema.springboot.hotel;

import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.hotel.service.HotelInfoService;
import de.mathema.springboot.hotel.service.HotelInfo;
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
public class HotelInfoEndpoints {

  private final HotelInfoService hotelInfoService;

  @GetMapping(path = "/hotel-infos", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<List<HotelInfo>> getAll() {
    return ResponseEntity.ok(hotelInfoService.getAll());
  }

  @PostMapping(path = "/hotel-infos", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<HotelInfo> saveHotelInfo(@RequestBody final HotelInfo hotelInfo) {
    return ResponseEntity.status(HttpStatus.CREATED).body(hotelInfoService.save(hotelInfo));
  }

  @GetMapping(path = "/hotel-infos/{hotel-info-id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<HotelInfo> getById(@PathVariable("hotel-info-id") final String hotelInfoId) {
    return ResponseEntity.ok(hotelInfoService.getById(hotelInfoId));
  }

  @PatchMapping(path = "/hotel-infos/{hotel-info-id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<HotelInfo> updateById(@PathVariable("hotel-info-id") final String hotelInfoId,
                                              @RequestBody final HotelInfo hotelInfo) {
    return ResponseEntity.ok(hotelInfoService.update(hotelInfoId, hotelInfo));
  }

  @PatchMapping(path = "/hotel-infos/{hotel-info-id}/quantity", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<EntityEvent> updateQuantity(@PathVariable("hotel-info-id") final String hotelInfoId,
                                                    @RequestBody final EntityEvent entityEvent) {
    return ResponseEntity.ok(hotelInfoService.updateQuantity(hotelInfoId, entityEvent));
  }
}
