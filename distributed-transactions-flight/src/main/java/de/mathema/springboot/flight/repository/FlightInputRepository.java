package de.mathema.springboot.flight.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Optional;

@Transactional(TxType.MANDATORY)
public interface FlightInputRepository extends PagingAndSortingRepository<FlightInput, String> {

  @Transactional(TxType.REQUIRES_NEW)
  default FlightInput saveFlightInput(FlightInput input) {
    return save(input);
  }

  default Optional<FlightInput> getFirstEvent() {
    Page<FlightInput> page = getEvents(PageRequest.of(0, 1));
    return page.stream().findFirst();
  }

  @Query("from FlightInput fi where fi.processed = false order by fi.created asc")
  Page<FlightInput> getEvents(Pageable pageable);
}
