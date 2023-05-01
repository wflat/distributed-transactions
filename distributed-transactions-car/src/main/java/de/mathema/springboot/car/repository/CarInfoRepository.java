package de.mathema.springboot.car.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@Transactional(TxType.MANDATORY)
public interface CarInfoRepository extends PagingAndSortingRepository<CarInfo, String> {

}
