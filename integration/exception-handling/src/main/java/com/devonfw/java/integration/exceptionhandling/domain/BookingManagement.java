package com.devonfw.java.integration.exceptionhandling.domain;

import com.devonfw.devon4j.generated.api.model.BookingTo;
import com.devonfw.java.integration.exceptionhandling.domain.model.Booking;
import com.devonfw.java.integration.exceptionhandling.general.exception.NotFoundException;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * Manages Bookings for our restaurant.
 */
@Component
public class BookingManagement {

  public Booking getBooking(Long id) {
    throw new NotImplementedException("Not implemented. The class only exists for mocking reasons in Unit Test");
  }

  public Booking createBooking(BookingTo bookingTo) {
    throw new NotImplementedException("Not implemented. The class only exists for mocking reasons in Unit Test");
  }
}
