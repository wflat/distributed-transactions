package de.mathema.springboot.travel.process;

import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.common.service.UuidService;
import de.mathema.springboot.travel.process.exception.ProcessInstanceNotStartedException;
import de.mathema.springboot.travel.process.model.ProcessInstance;
import de.mathema.springboot.travel.process.model.ProcessRequest;
import de.mathema.springboot.travel.process.model.Variable;
import de.mathema.springboot.travel.repository.TravelBooking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.boot.starter.ClientProperties;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CamundaService {

  static final String PROCESS_START_URL = "/process-definition/key/Process_TravelBooking/start";

  private final RestTemplate restTemplate;
  private final ClientProperties clientProperties;
  private final JsonService jsonService;
  private final UuidService uuidService;

  public ProcessInstance startProcessInstance(final TravelBooking travelBooking) {
    final ProcessRequest request = ProcessRequest.builder()
        .variables(
            Map.of(ProcessVariable.TRAVEL_BOOKING_ID.getVariablenname(), Variable.string(travelBooking.getId())))
        .businessKey(uuidService.uuid().toString())
        .build();
    final String requestJson = jsonService.json(request);

    final RequestEntity<String> requestEntity = RequestEntity
        .post(clientProperties.getBaseUrl() + PROCESS_START_URL)
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .body(requestJson);
    final ResponseEntity<ProcessInstance> response = exchange(requestEntity, ProcessInstance.class);
    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new ProcessInstanceNotStartedException(
          String.format("TravelBooking process-instance could not be started. Cause: %s",
              response.getStatusCode()));
    }

    final ProcessInstance process = response.getBody();
    log.info("Process-instance {} for TravelBooking with id {} started",
        Optional.ofNullable(process).map(ProcessInstance::getId).orElse(null), travelBooking.getId());
    return process;
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
