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
public interface FlightOutputRepository extends PagingAndSortingRepository<FlightOutput, String> {

  default Optional<FlightOutput> getFirstEvent() {
    Page<FlightOutput> page = getEvents(PageRequest.of(0, 1));
    return page.stream().findFirst();
  }

  @Query("from FlightOutput fo where fo.processed = false order by fo.created asc")
  Page<FlightOutput> getEvents(Pageable pageable);
}
