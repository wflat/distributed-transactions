package de.mathema.springboot.flight.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface FlightInfoMapper {

  FlightInfo toApi(de.mathema.springboot.flight.repository.FlightInfo flightInfo);

  default List<FlightInfo> toApi(List<de.mathema.springboot.flight.repository.FlightInfo> flightInfoList) {
    return flightInfoList.stream().map(this::toApi).toList();
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "version", ignore = true)
  de.mathema.springboot.flight.repository.FlightInfo toRepository(FlightInfo flightInfo);
}
