package de.mathema.springboot.travel.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface TravelBookingMapper {

  TravelBooking toApi(de.mathema.springboot.travel.repository.TravelBooking travelBooking);

  default List<TravelBooking> toApi(List<de.mathema.springboot.travel.repository.TravelBooking> travelBookingList) {
    return travelBookingList.stream().map(this::toApi).toList();
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "statusDetails", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "processInstanceId", ignore = true)
  @Mapping(target = "version", ignore = true)
  de.mathema.springboot.travel.repository.TravelBooking toRepository(TravelBooking travelBooking);
}
