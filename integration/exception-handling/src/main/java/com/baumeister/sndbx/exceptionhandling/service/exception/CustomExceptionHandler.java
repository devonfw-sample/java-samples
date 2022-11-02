package com.baumeister.sndbx.exceptionhandling.service.exception;

import com.devonfw.devon4j.generated.api.model.ValidationProblemDetailsTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;

@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ProblemDetailsTo> notFoundException(Throwable ex) {
      ProblemDetailsTo problemDetails = ProblemDetailsMapper.getInstance().map(ex);
      return new ResponseEntity<ProblemDetailsTo>(problemDetails, HttpStatus.valueOf(problemDetails.getStatus()));
  }

}
