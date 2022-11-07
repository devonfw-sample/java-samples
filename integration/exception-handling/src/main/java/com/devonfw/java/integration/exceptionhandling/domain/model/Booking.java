package com.devonfw.java.integration.exceptionhandling.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the booking of a table in our restaurant example.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
  private long id;
  private int numberOfSeats;
  private String description;
  private String email;
}
