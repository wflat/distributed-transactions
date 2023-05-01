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
public interface HotelInputRepository extends PagingAndSortingRepository<HotelInput, String> {

  @Transactional(TxType.REQUIRES_NEW)
  default HotelInput saveHotelInput(HotelInput input) {
    return save(input);
  }

  default Optional<HotelInput> getFirstEvent() {
    Page<HotelInput> page = getEvents(PageRequest.of(0, 1));
    return page.stream().findFirst();
  }

  @Query("from HotelInput hi where hi.processed = false order by hi.created asc")
  Page<HotelInput> getEvents(Pageable pageable);
}
