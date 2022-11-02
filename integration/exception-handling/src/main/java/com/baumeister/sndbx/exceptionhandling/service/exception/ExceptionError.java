package com.baumeister.sndbx.exceptionhandling.service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ExceptionError {

  private String type;
  private String title;
  private HttpStatus status;
}
