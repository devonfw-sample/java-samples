package com.devonfw.java.integration.exceptionhandling.service.exception;

import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;
import com.devonfw.devon4j.generated.api.model.ValidationProblemDetailsTo;
import com.devonfw.java.integration.exceptionhandling.general.exception.BusinessException;
import com.devonfw.java.integration.exceptionhandling.general.exception.NotFoundException;
import com.devonfw.java.integration.exceptionhandling.service.exception.ExceptionMapper.ExceptionMapperBuilder;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Contains all defined Mappers for an Exception to the corresponding ProblemDetailsTos
 * <p>
 * In this class define all Mappers as constant with a good name to understand the purpose.
 * <p>
 * Add them to the list at the bottom. A static Map will be created from that list for easier access
 * of the right mapper for a given exception.
 */
public abstract class ExceptionMapperDefinitions {

  /**
   * Complex mapping to get the reason for the failure as list of string. Sadly the
   * MethodArgumentNotValidException works a lot with wrappers making it hard to simply get the
   * cause. This is just a showcase. Depending on the use case the message alone could be enough
   */
  private static final BiConsumer<MethodArgumentNotValidException, ValidationProblemDetailsTo> VALIDATION_PROBLEM_DETAILS_BI_CONSUMER =
      (ex, problem) -> {
        List<String> fields = ex.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getArguments)
            .flatMap(Stream::of)
            .filter(arg -> arg instanceof DefaultMessageSourceResolvable)
            .map(arg -> (DefaultMessageSourceResolvable) arg)
            .map(msg -> msg.getCode())
            .collect(Collectors.toList());
        problem.setFailedValidation(fields);
      };

  public static final ExceptionMapper<Throwable, ProblemDetailsTo> DEFAULT_EXCEPTION_MAPPER =
      ExceptionMapperBuilder.builder(Throwable.class, ProblemDetailsTo::new)
          .withDetailDefinition((throwable) -> "An unexpected error has occurred! We apologize any inconvenience. Please try again later.")
          .build();

  private static final ExceptionMapper<MethodArgumentNotValidException, ValidationProblemDetailsTo> VALIDATION_EXCEPTION_MAPPER =
      ExceptionMapperBuilder.builder(
              MethodArgumentNotValidException.class, ValidationProblemDetailsTo::new)
          .withType("urn:problem:validation-error")
          .withTitle("A validation failed")
          .withStatus(HttpStatus.NOT_ACCEPTABLE.value())
          .withDetailDefinition((ex) -> "Validation failed for " + ex.getBindingResult().getErrorCount() + " fields")
          .withAdditionalAttributeSetter(VALIDATION_PROBLEM_DETAILS_BI_CONSUMER) // more complex mapping functions can be extracted
          .build();
  private static final ExceptionMapper<BusinessException, ProblemDetailsTo> BUSINESS_DEFAULT_EXCEPTION_MAPPER =
      ExceptionMapperBuilder.builder(
              BusinessException.class, ProblemDetailsTo::new)
          .withType("urn:problem:bad-request")
          .withTitle("Bad Request")
          .withStatus(HttpStatus.BAD_REQUEST.value())
          .build();
  private static final ExceptionMapper<NotFoundException, ProblemDetailsTo> NOT_FOUND_EXCEPTION_MAPPER =
      ExceptionMapperBuilder.builder(
              NotFoundException.class, ProblemDetailsTo::new)
          .withType("urn:problem:not-found")
          .withTitle("Resource not found")
          .withStatus(HttpStatus.NOT_FOUND.value())
          .build();

  /**
   * Add all Mappers here!
   */
  private static final List<ExceptionMapper<? extends Throwable, ? extends ProblemDetailsTo>> EXCEPTION_MAPPER_LIST =
      List.of(
          DEFAULT_EXCEPTION_MAPPER,
          BUSINESS_DEFAULT_EXCEPTION_MAPPER,
          VALIDATION_EXCEPTION_MAPPER,
          NOT_FOUND_EXCEPTION_MAPPER
      );


  public static final Map<Class<? extends Throwable>, ExceptionMapper<? extends Throwable, ? extends ProblemDetailsTo>> EXCEPTION_ERRORS_MAP
      = EXCEPTION_MAPPER_LIST.stream()
      .collect(Collectors.toMap(ExceptionMapper::getThrowableClass, pdfh -> pdfh));
}
