package com.devonfw.java.integration.exceptionhandling.domain;

import com.devonfw.java.integration.exceptionhandling.domain.model.Booking;
import com.devonfw.java.integration.exceptionhandling.general.exception.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * Manages Bookings for our restaurant.
 */
@Component
public class BookingManagement {

  /**
   * Returns a concrete Booking for a given id.
   * Because this is an example implementation, always a new Booking is created and returned using the given id.
   *
   * @param id The id of the booking as identifier.
   * @return The booking with the given id.
   * @throws NotFoundException when the given id is 404 (HTTP not found ;-) ) then a NotFoundException is thrown.
   * @throws IllegalArgumentException when the id parameter is not set.
   */
  public Booking getBooking(Long id) {
    if (ObjectUtils.isEmpty(id)) {
      throw new IllegalArgumentException("No id was defined, when calling getBooking()");
    }
    if(id == 404L) {
      throw new NotFoundException("The element of id " + id + " could not be found");
    }
    return Booking.builder()
        .id(id)
        .email("m.mustermann@mail.com")
        .description("No gluten")
        .numberOfSeats(2)
        .build();
  }
}
