package de.mathema.springboot.travel.service;

import de.mathema.springboot.common.exception.BadGatewayException;
import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.travel.config.service.ServicesProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpBookingService {

  private final RestTemplate restTemplate;
  private final ServicesProperties servicesProperties;
  private final JsonService jsonService;

  public EntityEvent patchHotelEntity(final EntityEvent entityEvent) {
    return bookEntity(entityEvent, servicesProperties.getHotel());
  }

  public EntityEvent patchCarEntity(final EntityEvent entityEvent) {
    return bookEntity(entityEvent, servicesProperties.getCar());
  }

  public EntityEvent patchFlightEntity(final EntityEvent entityEvent) {
    return bookEntity(entityEvent, servicesProperties.getFlight());
  }

  EntityEvent bookEntity(final EntityEvent entityEvent, final ServicesProperties.ServiceInfo serviceInfo) {
    log.info("Send EntityEvent {}/{}/{} for TravelBooking {}: {}{}",
        entityEvent.getEventType(), entityEvent.getEntityId(), entityEvent.getEntityQuantity(),
        entityEvent.getTravelBookingId(), serviceInfo.getBaseUrl(), serviceInfo.getEntityPath());

    final RequestEntity<String> requestEntity = RequestEntity
        .patch(serviceInfo.getBaseUrl() + serviceInfo.getEntityPath(), entityEvent.getEntityId())
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .body(jsonService.json(entityEvent));
    final ResponseEntity<EntityEvent> response = exchange(requestEntity, EntityEvent.class);
    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new BadGatewayException(
          String.format("Could not send EntityEvent %s/%s/%s for TravelBooking %s. Cause: %s",
              entityEvent.getEventType(), entityEvent.getEntityId(), entityEvent.getEntityQuantity(),
              entityEvent.getTravelBookingId(), response.getStatusCode()));
    }

    final EntityEvent responseEvent = response.getBody();
    log.info("Received EntityEvent {}/{}/{} for TravelBooking {}",
        responseEvent.getEventType(), responseEvent.getEntityId(), responseEvent.getEntityQuantity(),
        responseEvent.getTravelBookingId());
    return responseEvent;
  }

  private <T> ResponseEntity<T> exchange(final RequestEntity<?> requestEntity,
                                         final Class<T> responseType) {
    try {
      return restTemplate.exchange(requestEntity, responseType);
    } catch (final RestClientException exc) {
      if (exc instanceof HttpStatusCodeException statusCodeException) {
        ResponseEntity.status(statusCodeException.getStatusCode()).build();
      }
    }
    return ResponseEntity.internalServerError().build();
  }
}
