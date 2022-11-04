package com.devonfw.java.integration.exceptionhandling.service.exception;

import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;
import com.devonfw.devon4j.generated.api.model.ValidationProblemDetailsTo;
import com.devonfw.java.integration.exceptionhandling.general.exception.NotFoundException;
import com.devonfw.java.integration.exceptionhandling.service.exception.ExceptionMapper.ExceptionMapperBuilder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

public abstract class ExceptionMappers {

  private static final ExceptionMapper<Throwable, ProblemDetailsTo> DEFAULT_EXCEPTION_MAPPER =
      ExceptionMapperBuilder.builder(Throwable.class,
              ProblemDetailsTo.class)
          .withFactory(ProblemDetailsTo::new)
          .build();

  private static final ExceptionMapper<MethodArgumentNotValidException, ValidationProblemDetailsTo> VALIDATION_EXCEPTION_MAPPER =
      ExceptionMapperBuilder.builder(
              MethodArgumentNotValidException.class, ValidationProblemDetailsTo.class)
          .withFactory(ValidationProblemDetailsTo::new)
          .withType("validation-error")
          .withTitle("A validation failed")
          .withStatus(HttpStatus.NOT_ACCEPTABLE.value())
          .withDetailSetter((ex, problem) -> {
            problem.setDetail(
                "Validation failed for " + ex.getBindingResult().getErrorCount() + " fields");
          })
          .withAdditionalAttributeSetter(
              (ex, problem) -> {
                List<String> fields = ex.getBindingResult().getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getArguments)
                    .flatMap(Stream::of)
                    .filter(arg -> arg instanceof DefaultMessageSourceResolvable)
                    .map(arg -> (DefaultMessageSourceResolvable) arg)
                    .map(msg -> msg.getCode())
                    .collect(Collectors.toList());
                problem.setFailedValidation(fields);
              }
          )
          .build();
  private static final ExceptionMapper<NotFoundException, ProblemDetailsTo> NOT_FOUND_EXCEPTION_MAPPER =
      ExceptionMapperBuilder.builder(
              NotFoundException.class, ProblemDetailsTo.class)
          .withFactory(ProblemDetailsTo::new)
          .withType("not-found")
          .withTitle("Resource not found")
          .withStatus(HttpStatus.NOT_FOUND.value())
          .build();

  private static final List<ExceptionMapper<? extends Throwable, ? extends ProblemDetailsTo>> EXCEPTION_MAPPER_LIST =
      List.of(
          DEFAULT_EXCEPTION_MAPPER,
          VALIDATION_EXCEPTION_MAPPER,
          NOT_FOUND_EXCEPTION_MAPPER
      );


  public static final Map<Class<? extends Throwable>, ExceptionMapper<? extends Throwable, ? extends ProblemDetailsTo>> EXCEPTION_ERRORS_MAP
      = EXCEPTION_MAPPER_LIST.stream()
      .collect(Collectors.toMap(ExceptionMapper::getThrowableClass, pdfh -> pdfh));
}
