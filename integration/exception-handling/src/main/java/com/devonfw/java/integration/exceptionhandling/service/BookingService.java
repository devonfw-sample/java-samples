package com.devonfw.java.integration.exceptionhandling.service;

import com.devonfw.java.integration.exceptionhandling.domain.BookingManagement;
import com.devonfw.java.integration.exceptionhandling.general.exception.OverBookedException;
import com.devonfw.java.integration.exceptionhandling.service.mapper.BookingToMapper;
import com.devonfw.devon4j.generated.api.model.BookingTo;
import com.devonfw.devon4j.generated.api.service.BookingApi;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@RestController
public class BookingService implements com.devonfw.devon4j.generated.api.service.BookingApi {
  private BookingManagement bookingManagement;

  public BookingService(BookingManagement bookingManagement) {
    this.bookingManagement = bookingManagement;
  }
  @Override
  public Optional<NativeWebRequest> getRequest() {
    return BookingApi.super.getRequest();
  }

  @Override
  public ResponseEntity<BookingTo> createBooking(BookingTo bookingTo) {
    // Throwing this exception always, when the validation is ok
    throw new OverBookedException("Sadly there's no free table at the moment");
  }

  @Override
  public ResponseEntity<BookingTo> getBookingById(Long bookingId) {
    return ResponseEntity.accepted().body(BookingToMapper.INSTANCE.map(bookingManagement.getBooking(bookingId)));
  }
}
