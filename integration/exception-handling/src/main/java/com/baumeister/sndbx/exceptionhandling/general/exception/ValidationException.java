package com.baumeister.sndbx.exceptionhandling.general.exception;

import java.util.List;
import lombok.Getter;

public class ValidationException extends RuntimeException{
  private List<String> validationErrors;

  public ValidationException(String message, Throwable cause, List<String> validationErrors) {
    super(message, cause);
    this.validationErrors = validationErrors;
  }

  public List<String> getValidationErrors() {
    return validationErrors;
  }

  public void setValidationErrors(List<String> validationErrors) {
    this.validationErrors = validationErrors;
  }
}
