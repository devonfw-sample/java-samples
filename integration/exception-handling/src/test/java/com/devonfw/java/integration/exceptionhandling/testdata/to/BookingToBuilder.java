package com.devonfw.java.integration.exceptionhandling.testdata.to;

import com.devonfw.devon4j.generated.api.model.BookingTo;

public final class BookingToBuilder {

  private Long id = 1L;
  private Integer numberOfSeats = 10;
  private String description = "Only veggi";
  private String email = "max.mustermann@mail.de";

  private BookingToBuilder() {
  }

  public static BookingToBuilder aBookingTo() {
    return new BookingToBuilder();
  }

  public BookingToBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public BookingToBuilder withNumberOfSeats(Integer numberOfSeats) {
    this.numberOfSeats = numberOfSeats;
    return this;
  }

  public BookingToBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public BookingToBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public BookingTo build() {
    BookingTo bookingTo = new BookingTo();
    bookingTo.setId(id);
    bookingTo.setNumberOfSeats(numberOfSeats);
    bookingTo.setDescription(description);
    bookingTo.setEmail(email);
    return bookingTo;
  }
}
