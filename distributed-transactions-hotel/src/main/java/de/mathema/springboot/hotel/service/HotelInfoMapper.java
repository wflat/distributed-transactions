package de.mathema.springboot.hotel.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface HotelInfoMapper {

  HotelInfo toApi(de.mathema.springboot.hotel.repository.HotelInfo hotelInfo);

  default List<HotelInfo> toApi(List<de.mathema.springboot.hotel.repository.HotelInfo> hotelInfoList) {
    return hotelInfoList.stream().map(this::toApi).toList();
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "version", ignore = true)
  de.mathema.springboot.hotel.repository.HotelInfo toRepository(HotelInfo hotelInfo);
}
