package de.mathema.springboot.travel.service;

import de.mathema.springboot.travel.config.service.ServicesProperties;
import de.mathema.springboot.travel.process.CamundaService;
import de.mathema.springboot.travel.process.health.CamundaRequired;
import de.mathema.springboot.travel.repository.TravelBookingRepository;
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
public class TravelBookingService {

  private final TravelBookingRepository travelBookingRepository;
  private final CamundaService camundaService;
  private final SaveTravelBookingAction saveTravelBookingAction;
  private final TravelBookingMapper travelBookingMapper;
  private final ServicesProperties servicesProperties;

  @Transactional(TxType.NEVER)
  @CamundaRequired
  public TravelBooking save(final TravelBooking travelBooking) {
    final de.mathema.springboot.travel.repository.TravelBooking input = travelBookingMapper.toRepository(travelBooking);
    final de.mathema.springboot.travel.repository.TravelBooking savedTravelBooking = saveTravelBookingAction.save(input);
    if (!servicesProperties.isUseEvents()) {
      camundaService.startProcessInstance(savedTravelBooking);
    }
    return travelBookingMapper.toApi(savedTravelBooking);
  }

  @Transactional(TxType.REQUIRED)
  public TravelBooking getById(final String travelBookingId) {
    return travelBookingMapper.toApi(travelBookingRepository.getTravelBooking(travelBookingId));
  }

  @Transactional(TxType.REQUIRED)
  public List<TravelBooking> getAll() {
    return travelBookingMapper.toApi(
        StreamSupport.stream(travelBookingRepository.findAll().spliterator(), false).toList());
  }
}
