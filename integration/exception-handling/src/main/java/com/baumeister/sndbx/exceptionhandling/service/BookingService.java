package com.baumeister.sndbx.exceptionhandling.service;

import com.baumeister.sndbx.exceptionhandling.domain.BookingManagement;
import com.baumeister.sndbx.exceptionhandling.service.mapper.BookingToMapper;
import com.devonfw.devon4j.generated.api.model.BookingTo;
import com.devonfw.devon4j.generated.api.service.BookingApi;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    return ResponseEntity.accepted().body(bookingTo);
  }

  @Override
  public ResponseEntity<List<BookingTo>> getBookingAll() {
    return BookingApi.super.getBookingAll();
  }

  @Override
  public ResponseEntity<BookingTo> getBookingById(Long bookingId) {
    return ResponseEntity.accepted().body(BookingToMapper.INSTANCE.map(bookingManagement.getBooking(bookingId)));
  }
}
