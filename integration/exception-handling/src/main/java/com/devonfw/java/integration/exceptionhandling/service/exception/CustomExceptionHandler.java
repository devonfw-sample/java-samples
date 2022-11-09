package com.devonfw.java.integration.exceptionhandling.service.exception;

import static com.devonfw.java.integration.exceptionhandling.service.exception.ExceptionMapperDefinitions.DEFAULT_EXCEPTION_MAPPER;
import static com.devonfw.java.integration.exceptionhandling.service.exception.ExceptionMapperDefinitions.EXCEPTION_ERRORS_MAP;

import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Catches the exceptions and maps them to Problemdetails (see RFC7807).
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  /**
   * This exceptionHandler catches all Throwables, uses the possible next mapper and returns the according
   * ProblemdetailsTo. Problemdetails follow the RFC 7807.
   *
   * @param ex The thrown exception.
   * @return A {@link ProblemDetailsTo} as {@link ResponseEntity} and the according {@link HttpStatus}
   */
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ProblemDetailsTo> catchThrowable(Throwable ex) {
    ExceptionMapper mapper = getExceptionMapper(ex);
    ProblemDetailsTo problemDetails = mapper.map(ex);

    return new ResponseEntity(problemDetails,
        HttpStatus.valueOf(problemDetails.getStatus()));
  }

  private static ExceptionMapper<? extends Throwable, ? extends ProblemDetailsTo>
      getExceptionMapper(Throwable ex) {

    Class<?> exceptionClazz = ex.getClass();
    ExceptionMapper<? extends Throwable, ? extends ProblemDetailsTo> mapper = EXCEPTION_ERRORS_MAP.get(
        exceptionClazz);

    // Check if an ExceptionMapper exists that is applicable.
    // This should in the last instance always be Throwable
    while (Throwable.class.isAssignableFrom(exceptionClazz) && mapper == null) {
      mapper = EXCEPTION_ERRORS_MAP.get(exceptionClazz);
      exceptionClazz = exceptionClazz.getSuperclass();
    }

    if (mapper == null) {
      // defining a default.
      // But this should also never happen, as every exception should be derivable from Throwable.
      // Anyway as we're in the exception mapping phase, we're implementing it paranoid.
      log.warn("Could not find an accurate exception mapper for {}", ex.getClass());
      mapper = DEFAULT_EXCEPTION_MAPPER;
    }

    return mapper;
  }

}
