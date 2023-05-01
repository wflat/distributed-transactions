package de.mathema.springboot.travel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Optional;

@Transactional(TxType.MANDATORY)
public interface TravelOutputRepository extends PagingAndSortingRepository<TravelOutput, String> {

  default Optional<TravelOutput> getFirstEvent() {
    Page<TravelOutput> page = getEvents(PageRequest.of(0, 1));
    return page.stream().findFirst();
  }

  @Query("from TravelOutput to where to.processed = false order by to.created asc")
  Page<TravelOutput> getEvents(Pageable pageable);
}
