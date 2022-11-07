package com.devonfw.java.integration.exceptionhandling.general.exception;

public abstract class BusinessException extends RuntimeException{

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }
}
