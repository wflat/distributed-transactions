package de.mathema.springboot.car.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface CarInfoMapper {

  CarInfo toApi(de.mathema.springboot.car.repository.CarInfo carInfo);

  default List<CarInfo> toApi(List<de.mathema.springboot.car.repository.CarInfo> carInfoList) {
    return carInfoList.stream().map(this::toApi).toList();
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "version", ignore = true)
  de.mathema.springboot.car.repository.CarInfo toRepository(CarInfo carInfo);
}
