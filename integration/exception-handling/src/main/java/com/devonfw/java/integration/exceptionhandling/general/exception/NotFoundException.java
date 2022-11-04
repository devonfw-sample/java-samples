package com.devonfw.java.integration.exceptionhandling.general.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }
}
