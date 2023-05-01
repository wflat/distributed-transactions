package de.mathema.springboot.flight.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@Transactional(TxType.MANDATORY)
public interface FlightInfoRepository extends PagingAndSortingRepository<FlightInfo, String> {

}
