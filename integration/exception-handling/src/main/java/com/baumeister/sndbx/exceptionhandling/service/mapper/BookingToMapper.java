package com.baumeister.sndbx.exceptionhandling.service.mapper;

import com.baumeister.sndbx.exceptionhandling.domain.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingToMapper {
  BookingToMapper INSTANCE = Mappers.getMapper(BookingToMapper.class);

  com.devonfw.devon4j.generated.api.model.BookingTo map(Booking source);
}
