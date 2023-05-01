package de.mathema.springboot.hotel.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@Transactional(TxType.MANDATORY)
public interface HotelInfoRepository extends PagingAndSortingRepository<HotelInfo, String> {

}
