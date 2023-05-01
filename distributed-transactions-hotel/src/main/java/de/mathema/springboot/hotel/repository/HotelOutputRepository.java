package de.mathema.springboot.hotel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Optional;

@Transactional(TxType.MANDATORY)
public interface HotelOutputRepository extends PagingAndSortingRepository<HotelOutput, String> {

  default Optional<HotelOutput> getFirstEvent() {
    Page<HotelOutput> page = getEvents(PageRequest.of(0, 1));
    return page.stream().findFirst();
  }

  @Query("from HotelOutput ho where ho.processed = false order by ho.created asc")
  Page<HotelOutput> getEvents(Pageable pageable);
}
