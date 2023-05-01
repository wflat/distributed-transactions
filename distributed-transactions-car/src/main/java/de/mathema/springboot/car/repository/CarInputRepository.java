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
public interface CarInputRepository extends PagingAndSortingRepository<CarInput, String> {

  @Transactional(TxType.REQUIRES_NEW)
  default CarInput saveCarInput(CarInput input) {
    return save(input);
  }

  default Optional<CarInput> getFirstEvent() {
    Page<CarInput> page = getEvents(PageRequest.of(0, 1));
    return page.stream().findFirst();
  }

  @Query("from CarInput ci where ci.processed = false order by ci.created asc")
  Page<CarInput> getEvents(Pageable pageable);
}
