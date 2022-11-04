package com.devonfw.java.integration.exceptionhandling.service.exception;

import static com.devonfw.java.integration.exceptionhandling.service.exception.ExceptionMappers.EXCEPTION_ERRORS_MAP;

import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;
import com.devonfw.java.integration.exceptionhandling.general.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

  private static ExceptionMapper<? extends Throwable, ? extends ProblemDetailsTo> getExceptionMapper(
      Throwable ex) {
    Class<?> exceptionClazz = ex.getClass();
    ExceptionMapper<? extends Throwable, ? extends ProblemDetailsTo> mapper = EXCEPTION_ERRORS_MAP.get(
        exceptionClazz);
    while (Throwable.class.isAssignableFrom(exceptionClazz) && mapper == null) {
      mapper = EXCEPTION_ERRORS_MAP.get(exceptionClazz);
      exceptionClazz = exceptionClazz.getSuperclass();
    }

    if (mapper == null) {
      mapper = EXCEPTION_ERRORS_MAP.get(Throwable.class);
    }

    return mapper;
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ProblemDetailsTo> catchThrowable(Throwable ex) {
    ExceptionMapper mapper = getExceptionMapper(ex);
    ProblemDetailsTo problemDetails = mapper.map(ex);

    return new ResponseEntity(problemDetails,
        HttpStatus.valueOf(problemDetails.getStatus()));
  }

}
