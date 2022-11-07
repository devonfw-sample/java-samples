package com.devonfw.java.integration.exceptionhandling.general.exception;

public class OverBookedException extends BusinessException{

  public OverBookedException(String message) {
    super(message);
  }

  public OverBookedException(String message, Throwable cause) {
    super(message, cause);
  }
}
