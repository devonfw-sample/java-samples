package com.baumeister.sndbx.exceptionhandling.service.exception;

import com.baumeister.sndbx.exceptionhandling.general.exception.NotFoundException;
import com.baumeister.sndbx.exceptionhandling.general.exception.ValidationException;
import com.baumeister.sndbx.exceptionhandling.service.exception.ProblemDetailsFactoryHelper.ProblemDetailsFactoryHelperBuilder;
import com.devonfw.devon4j.generated.api.model.ValidationProblemDetailsTo;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.mapstruct.factory.Mappers;
import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ProblemDetailsMapper {

  private static ProblemDetailsMapper instance;

  private ProblemDetailsMapper() {
  }

  public static ProblemDetailsMapper getInstance() {
    if (instance == null) {
      instance = new ProblemDetailsMapper();
    }
    return instance;
  }

  private static final ProblemDetailsFactoryHelper<Throwable, ProblemDetailsTo> defaultFactory =
      ProblemDetailsFactoryHelperBuilder.aProblemDetailsFactoryHelper(Throwable.class,
              ProblemDetailsTo.class)
          .withFactory(ProblemDetailsTo::new)
          .build();
  Map<Class<? extends Throwable>, ProblemDetailsFactoryHelper> EXCEPTION_ERROR_MAP = Map.of(

      MethodArgumentNotValidException.class,
      ProblemDetailsFactoryHelperBuilder.aProblemDetailsFactoryHelper(
              MethodArgumentNotValidException.class, ValidationProblemDetailsTo.class)
          .withFactory(ValidationProblemDetailsTo::new)
          .withType("validation-error")
          .withTitle("A validation failed")
          .withStatus(HttpStatus.NOT_ACCEPTABLE.value())
          .withAdditionalAttributeSetter(
              (ex, problem) -> {
                problem.setFailedValidation(Arrays.asList(ex.getSuppressedFields()));
              }
          )

          .build()
  );

  public ProblemDetailsTo map(Throwable ex) {
    ProblemDetailsFactoryHelper helper = EXCEPTION_ERROR_MAP.get(ex.getClass());

    if (helper == null) {
      helper = defaultFactory;
    }

    ProblemDetailsTo problemDetails = (ProblemDetailsTo) helper.getFactory().get();


    helper.getTypeSetter().accept(ex, problemDetails);
    helper.getTitleSetter().accept(ex, problemDetails);
    helper.getDetailSetter().accept(ex, problemDetails);
    helper.getStatusSetter().accept(ex, problemDetails);
    helper.getInstanceSetter().accept(ex, problemDetails);
    if(helper.getAdditionalAttributeSetter() != null) {
      helper.getAdditionalAttributeSetter().accept(ex, problemDetails);
    }

    return problemDetails;
  }

}