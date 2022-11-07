package com.devonfw.java.integration.exceptionhandling.general.exception;

/**
 * Exception when a certain element was not found.
 */
public class NotFoundException extends BusinessException {

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
