package de.mathema.springboot.car.service;

import de.mathema.springboot.car.repository.CarInfo;
import de.mathema.springboot.car.repository.CarInfoNotFoundException;
import de.mathema.springboot.car.repository.CarInfoRepository;
import de.mathema.springboot.common.exception.BadRequestException;
import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarInfoService {

  private final CarInfoRepository carInfoRepository;
  private final CarInfoMapper carInfoMapper;

  @Transactional(TxType.REQUIRED)
  public List<de.mathema.springboot.car.service.CarInfo> getAll() {
    return carInfoMapper.toApi(
        StreamSupport.stream(carInfoRepository.findAll().spliterator(), false).toList());
  }

  @Transactional(TxType.REQUIRED)
  public de.mathema.springboot.car.service.CarInfo getById(final String carInfoId) {
    return carInfoMapper.toApi(getCarInfo(carInfoId));
  }

  @Transactional(TxType.REQUIRED)
  public de.mathema.springboot.car.service.CarInfo save(final de.mathema.springboot.car.service.CarInfo carInfo) {
    final CarInfo input = carInfoMapper.toRepository(carInfo);
    final CarInfo savedCarInfo = carInfoRepository.save(input);
    return carInfoMapper.toApi(savedCarInfo);
  }

  @Transactional(TxType.REQUIRED)
  public de.mathema.springboot.car.service.CarInfo update(final String carInfoId, final de.mathema.springboot.car.service.CarInfo carInfo) {
    final CarInfo savedCarInfo = getCarInfo(carInfoId);
    savedCarInfo.setQuantity(carInfo.getQuantity());
    savedCarInfo.setDescription(carInfo.getDescription());
    return carInfoMapper.toApi(savedCarInfo);
  }

  @Transactional(TxType.REQUIRED)
  public EntityEvent updateQuantity(final String carInfoId, final EntityEvent event) {
    log.info("Process EntityEvent: {}/{}/{}",
        event.getEventType(), event.getEntityId(), event.getEntityQuantity());

    try {
      final CarInfo carInfo = getCarInfo(carInfoId);

      // TODO: EntityEvent verarbeiten, CarInfo aktualisieren und Response erstellen
      return null;
    } catch (final Exception exc) {
      log.error("Exception: {}", exc.getMessage());
      return createFailureEvent(event, exc.getMessage());
    }
  }

  EntityEvent handleUnexpectedEvent(final EntityEvent event) {
    final BadRequestException exception = new BadRequestException(
        String.format("Unexpected CarEvent received %s/%s for TravelBooking %s",
            event.getEventId(), event.getEventType(), event.getTravelBookingId()));
    log.warn(exception.getMessage());
    throw exception;
  }

  EntityEvent createFailureEvent(final EntityEvent event, final String message) {
    return createOutputEvent(EventType.FAILURE, event.getTravelBookingId(), event.getEntityId(),
        event.getEntityQuantity(), message);
  }

  EntityEvent createOutputEvent(final EventType eventType, final String travelBookingId,
                                final String entityId, final int entityQuantity, final String message) {
    log.info("Create OutputEvent: {}/{}/{}/{} for TravelBooking {}",
        eventType, entityId, entityQuantity, message, travelBookingId);

    return EntityEvent.builder()
        .eventType(eventType)
        .travelBookingId(travelBookingId)
        .entityId(entityId)
        .entityQuantity(entityQuantity)
        .message(message)
        .build();
  }

  CarInfo getCarInfo(final String id) {
    return carInfoRepository.findById(id)
        .orElseThrow(() -> new CarInfoNotFoundException(String.format("CarInfo with id %s not found", id)));
  }
}
