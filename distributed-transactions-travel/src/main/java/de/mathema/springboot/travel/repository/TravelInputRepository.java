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
public interface TravelInputRepository extends PagingAndSortingRepository<TravelInput, String> {

  @Transactional(TxType.REQUIRES_NEW)
  default TravelInput saveTravelInput(TravelInput input) {
    return save(input);
  }

  default Optional<TravelInput> getFirstEvent() {
    Page<TravelInput> page = getEvents(PageRequest.of(0, 1));
    return page.stream().findFirst();
  }

  @Query("from TravelInput ti where ti.processed = false order by ti.created asc")
  Page<TravelInput> getEvents(Pageable pageable);
}
