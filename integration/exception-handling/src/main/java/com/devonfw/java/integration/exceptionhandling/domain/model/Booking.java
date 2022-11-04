package com.devonfw.java.integration.exceptionhandling.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
