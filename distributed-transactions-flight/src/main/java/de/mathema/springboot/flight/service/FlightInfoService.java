package de.mathema.springboot.flight.service;

import de.mathema.springboot.common.exception.BadRequestException;
import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import de.mathema.springboot.flight.repository.FlightInfoNotFoundException;
import de.mathema.springboot.flight.repository.FlightInfoRepository;
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
public class FlightInfoService {

  private final FlightInfoRepository flightInfoRepository;
  private final FlightInfoMapper flightInfoMapper;

  @Transactional(TxType.REQUIRED)
  public List<FlightInfo> getAll() {
    return flightInfoMapper.toApi(
        StreamSupport.stream(flightInfoRepository.findAll().spliterator(), false).toList());
  }

  @Transactional(TxType.REQUIRED)
  public FlightInfo getById(final String flightInfoId) {
    return flightInfoMapper.toApi(getFlightInfo(flightInfoId));
  }

  @Transactional(TxType.REQUIRED)
  public FlightInfo save(final FlightInfo flightInfo) {
    final de.mathema.springboot.flight.repository.FlightInfo input = flightInfoMapper.toRepository(flightInfo);
    final de.mathema.springboot.flight.repository.FlightInfo savedFlightInfo = flightInfoRepository.save(input);
    return flightInfoMapper.toApi(savedFlightInfo);
  }

  @Transactional(TxType.REQUIRED)
  public FlightInfo update(final String flightInfoId, final FlightInfo flightInfo) {
    final de.mathema.springboot.flight.repository.FlightInfo savedFlightInfo = getFlightInfo(flightInfoId);
    savedFlightInfo.setQuantity(flightInfo.getQuantity());
    savedFlightInfo.setDescription(flightInfo.getDescription());
    return flightInfoMapper.toApi(savedFlightInfo);
  }

  @Transactional(TxType.REQUIRED)
  public EntityEvent updateQuantity(final String flightInfoId, final EntityEvent event) {
    log.info("Process EntityEvent: {}/{}/{}",
        event.getEventType(), event.getEntityId(), event.getEntityQuantity());

    try {
      final de.mathema.springboot.flight.repository.FlightInfo flightInfo = getFlightInfo(flightInfoId);

      // TODO: EntityEvent verarbeiten, FlightInfo aktualisieren und Response erstellen
      return null;
    } catch (final Exception exc) {
      log.error("Exception: {}", exc.getMessage());
      return createFailureEvent(event, exc.getMessage());
    }
  }

  EntityEvent handleUnexpectedEvent(final EntityEvent event) {
    final BadRequestException exception = new BadRequestException(
        String.format("Unexpected FlightEvent received %s/%s for TravelBooking %s",
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

  de.mathema.springboot.flight.repository.FlightInfo getFlightInfo(final String id) {
    return flightInfoRepository.findById(id)
        .orElseThrow(() -> new FlightInfoNotFoundException(String.format("FlightInfo with id %s not found", id)));
  }
}
