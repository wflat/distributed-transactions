package de.mathema.springboot.car;

import de.mathema.springboot.car.service.CarInfo;
import de.mathema.springboot.car.service.CarInfoService;
import de.mathema.springboot.common.model.EntityEvent;
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
public class CarInfoEndpoints {

  private final CarInfoService carInfoService;

  @GetMapping(path = "/car-infos", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<List<CarInfo>> getAll() {
    return ResponseEntity.ok(carInfoService.getAll());
  }

  @PostMapping(path = "/car-infos", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<CarInfo> saveCarInfo(@RequestBody final CarInfo carInfo) {
    return ResponseEntity.status(HttpStatus.CREATED).body(carInfoService.save(carInfo));
  }

  @GetMapping(path = "/car-infos/{car-info-id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<CarInfo> getById(@PathVariable("car-info-id") final String carInfoId) {
    return ResponseEntity.ok(carInfoService.getById(carInfoId));
  }

  @PatchMapping(path = "/car-infos/{car-info-id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<CarInfo> updateById(@PathVariable("car-info-id") final String carInfoId,
                                            @RequestBody final CarInfo carInfo) {
    return ResponseEntity.ok(carInfoService.update(carInfoId, carInfo));
  }

  @PatchMapping(path = "/car-infos/{car-info-id}/quantity", produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<EntityEvent> updateQuantity(@PathVariable("car-info-id") final String carInfoId,
                                                    @RequestBody final EntityEvent entityEvent) {
    return ResponseEntity.ok(carInfoService.updateQuantity(carInfoId, entityEvent));
  }
}
