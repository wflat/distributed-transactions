package de.mathema.springboot.hotel.service;

import de.mathema.springboot.common.exception.BadRequestException;
import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.model.EventType;
import de.mathema.springboot.hotel.repository.HotelInfoNotFoundException;
import de.mathema.springboot.hotel.repository.HotelInfoRepository;
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
public class HotelInfoService {

  private final HotelInfoRepository hotelInfoRepository;
  private final HotelInfoMapper hotelInfoMapper;

  @Transactional(TxType.REQUIRED)
  public List<HotelInfo> getAll() {
    return hotelInfoMapper.toApi(
        StreamSupport.stream(hotelInfoRepository.findAll().spliterator(), false).toList());
  }

  @Transactional(TxType.REQUIRED)
  public HotelInfo getById(final String hotelInfoId) {
    return hotelInfoMapper.toApi(getHotelInfo(hotelInfoId));
  }

  @Transactional(TxType.REQUIRED)
  public HotelInfo save(final HotelInfo hotelInfo) {
    final de.mathema.springboot.hotel.repository.HotelInfo input = hotelInfoMapper.toRepository(hotelInfo);
    final de.mathema.springboot.hotel.repository.HotelInfo savedHotelInfo = hotelInfoRepository.save(input);
    return hotelInfoMapper.toApi(savedHotelInfo);
  }

  @Transactional(TxType.REQUIRED)
  public HotelInfo update(final String hotelInfoId, final HotelInfo hotelInfo) {
    final de.mathema.springboot.hotel.repository.HotelInfo savedHotelInfo = getHotelInfo(hotelInfoId);
    savedHotelInfo.setQuantity(hotelInfo.getQuantity());
    savedHotelInfo.setDescription(hotelInfo.getDescription());
    return hotelInfoMapper.toApi(savedHotelInfo);
  }

  @Transactional(TxType.REQUIRED)
  public EntityEvent updateQuantity(final String hotelInfoId, final EntityEvent event) {
    log.info("Process EntityEvent: {}/{}/{}",
        event.getEventType(), event.getEntityId(), event.getEntityQuantity());

    try {
      final de.mathema.springboot.hotel.repository.HotelInfo hotelInfo = getHotelInfo(hotelInfoId);

      // TODO: EntityEvent verarbeiten, HotelInfo aktualisieren und Response erstellen
      return null;
    } catch (final Exception exc) {
      log.error("Exception: {}", exc.getMessage());
      return createFailureEvent(event, exc.getMessage());
    }
  }

  EntityEvent handleUnexpectedEvent(final EntityEvent event) {
    final BadRequestException exception = new BadRequestException(
        String.format("Unexpected HotelEvent received %s/%s for TravelBooking %s",
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

  de.mathema.springboot.hotel.repository.HotelInfo getHotelInfo(final String id) {
    return hotelInfoRepository.findById(id)
        .orElseThrow(() -> new HotelInfoNotFoundException(String.format("HotelInfo with id %s not found", id)));
  }
}
