package com.devonfw.java.integration.exceptionhandling.domain;

import com.devonfw.java.integration.exceptionhandling.domain.model.Booking;
import com.devonfw.java.integration.exceptionhandling.general.exception.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class BookingManagement {

  public Booking getBooking(Long id) {
    ObjectUtils.isEmpty(id);
    if(id == 404L) {
      throw new NotFoundException("The element of id " + id + " could not be found");
    }
    return Booking.builder()
        .id(1L)
        .email("m.mustermann@mail.com")
        .description("No gluten")
        .numberOfSeats(2)
        .build();
  }

  public Booking createBooking(Booking booking) {
    return booking;
  }
}
