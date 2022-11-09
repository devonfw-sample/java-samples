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

  public static final BookingToMapper TO_MAPPER = BookingToMapper.INSTANCE;
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
    return ResponseEntity.accepted().body(TO_MAPPER.map(bookingManagement.createBooking(bookingTo)));
  }

  @Override
  public ResponseEntity<BookingTo> getBookingById(Long bookingId) {
    return ResponseEntity.accepted().body(TO_MAPPER.map(bookingManagement.getBooking(bookingId)));
  }
}
