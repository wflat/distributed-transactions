package de.mathema.springboot.car.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Optional;

@Transactional(TxType.MANDATORY)
public interface CarOutputRepository extends PagingAndSortingRepository<CarOutput, String> {

  default Optional<CarOutput> getFirstEvent() {
    Page<CarOutput> page = getEvents(PageRequest.of(0, 1));
    return page.stream().findFirst();
  }

  @Query("from CarOutput co where co.processed = false order by co.created asc")
  Page<CarOutput> getEvents(Pageable pageable);
}
