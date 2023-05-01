package de.mathema.springboot.travel.repository;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.repository.PagingAndSortingRepository;

@Transactional(TxType.MANDATORY)
public interface TravelBookingRepository extends PagingAndSortingRepository<TravelBooking, String> {

  default TravelBooking getTravelBooking(final String id) {
    return this.findById(id)
        .orElseThrow(() -> new TravelBookingNotFoundException(String.format("TravelBooking with id %s not found", id)));
  }
}
