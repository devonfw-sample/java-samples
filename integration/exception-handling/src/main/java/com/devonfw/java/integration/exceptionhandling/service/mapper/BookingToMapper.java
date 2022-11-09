package com.devonfw.java.integration.exceptionhandling.service.mapper;

import com.devonfw.java.integration.exceptionhandling.domain.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapstruct mapper for To to Model of the booking entity mapping.
 */
@Mapper
public interface BookingToMapper {
  BookingToMapper INSTANCE = Mappers.getMapper(BookingToMapper.class);

  com.devonfw.devon4j.generated.api.model.BookingTo map(Booking source);
}
