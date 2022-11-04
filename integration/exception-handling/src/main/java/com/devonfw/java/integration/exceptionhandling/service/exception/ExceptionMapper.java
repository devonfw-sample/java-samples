package com.devonfw.java.integration.exceptionhandling.service.exception;

import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ExceptionMapper<FROM extends Throwable, TO extends ProblemDetailsTo> {

  private Class<FROM> throwableClass;

  private Supplier<TO> factory;
  private String type = "urn:problem:internal-server-error";
  private String title = "Internal Server Error";
  private Integer status = 500;
  private BiConsumer<FROM, TO> typeSetter = (ex, problem) -> problem.setType(this.type);
  private BiConsumer<FROM, TO> titleSetter = (ex, problem) -> problem.setTitle(this.title);
  private BiConsumer<FROM, TO> instanceSetter = (ex, problem) -> problem.setInstance(
      UUID.randomUUID().toString());
  private BiConsumer<FROM, TO> statusSetter = (ex, problem) -> problem.setStatus(this.status);
  private BiConsumer<FROM, TO> detailSetter = (ex, problem) -> problem.setDetail(ex.getMessage());
  private BiConsumer<FROM, TO> additionalAttributeSetter;

  public TO map(FROM ex) {

    TO problemDetails = this.factory.get();
    this.typeSetter.accept(ex, problemDetails);
    this.titleSetter.accept(ex, problemDetails);
    this.detailSetter.accept(ex, problemDetails);
    this.statusSetter.accept(ex, problemDetails);
    this.instanceSetter.accept(ex, problemDetails);

    if (this.additionalAttributeSetter != null) {
      this.additionalAttributeSetter.accept(ex, problemDetails);
    }
    return problemDetails;
  }

  public Supplier<TO> getFactory() {
    return factory;
  }

  public String getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public Integer getStatus() {
    return status;
  }

  public BiConsumer<FROM, TO> getTypeSetter() {
    return typeSetter;
  }

  public BiConsumer<FROM, TO> getTitleSetter() {
    return titleSetter;
  }

  public BiConsumer<FROM, TO> getInstanceSetter() {
    return instanceSetter;
  }

  public BiConsumer<FROM, TO> getStatusSetter() {
    return statusSetter;
  }

  public BiConsumer<FROM, TO> getDetailSetter() {
    return detailSetter;
  }

  public BiConsumer<FROM, TO> getAdditionalAttributeSetter() {
    return additionalAttributeSetter;
  }

  public Class<FROM> getThrowableClass() {
    return throwableClass;
  }

  public static final class ExceptionMapperBuilder<FROM extends Throwable, TO extends ProblemDetailsTo> {

    private Class<FROM> throwableClass;

    private Supplier<TO> factory;
    private String type = "internal-server-error";
    private String title = "Internal Server Error";
    private Integer status = 500;
    private BiConsumer<FROM, TO> typeSetter = (ex, problem) -> problem.setType(
        "urn:problem:" + this.type);
    private BiConsumer<FROM, TO> titleSetter = (ex, problem) -> problem.setTitle(this.title);
    private BiConsumer<FROM, TO> instanceSetter = (ex, problem) -> problem.setInstance(
        "urn:uuid:" + UUID.randomUUID().toString());
    private BiConsumer<FROM, TO> statusSetter = (ex, problem) -> problem.setStatus(this.status);
    private BiConsumer<FROM, TO> detailSetter = (ex, problem) -> problem.setDetail(ex.getMessage());
    private BiConsumer<FROM, TO> additionalAttributeSetter;

    private ExceptionMapperBuilder(Class<FROM> throwableClass) {
      this.throwableClass = throwableClass;
    }

    public static <E extends Throwable, P extends ProblemDetailsTo> ExceptionMapperBuilder<E, P> builder(
        Class<E> throwableClass, Class<P> problemdetailsClazz) {
      return new ExceptionMapperBuilder<E, P>(throwableClass);
    }

    public ExceptionMapperBuilder<FROM, TO> withFactory(Supplier<TO> factory) {
      this.factory = factory;
      return this;
    }

    public ExceptionMapperBuilder<FROM, TO> withType(String type) {
      this.type = type;
      return this;
    }

    public ExceptionMapperBuilder<FROM, TO> withTitle(String title) {
      this.title = title;
      return this;
    }

    public ExceptionMapperBuilder<FROM, TO> withStatus(Integer status) {
      this.status = status;
      return this;
    }

    public ExceptionMapperBuilder<FROM, TO> withTypeSetter(BiConsumer<FROM, TO> typeSetter) {
      this.typeSetter = typeSetter;
      return this;
    }

    public ExceptionMapperBuilder<FROM, TO> withTitleSetter(BiConsumer<FROM, TO> titleSetter) {
      this.titleSetter = titleSetter;
      return this;
    }

    public ExceptionMapperBuilder<FROM, TO> withInstanceSetter(
        BiConsumer<FROM, TO> instanceSetter) {
      this.instanceSetter = instanceSetter;
      return this;
    }

    public ExceptionMapperBuilder<FROM, TO> withStatusSetter(BiConsumer<FROM, TO> statusSetter) {
      this.statusSetter = statusSetter;
      return this;
    }

    public ExceptionMapperBuilder<FROM, TO> withDetailSetter(BiConsumer<FROM, TO> detailSetter) {
      this.detailSetter = detailSetter;
      return this;
    }

    public ExceptionMapperBuilder<FROM, TO> withAdditionalAttributeSetter(
        BiConsumer<FROM, TO> additionalAttributeSetter) {
      this.additionalAttributeSetter = additionalAttributeSetter;
      return this;
    }

    public ExceptionMapper<FROM, TO> build() {
      if (this.factory == null) {
        // A factory needs to be defined for each ProblemDetailsTo.
        // Otherwise, the correct ProblemDetailsTo could not be initialized by the mapper.
        throw new IllegalArgumentException("Missing a factory for the ProblemDetails");
      }
      ExceptionMapper<FROM, TO> problemDetailsFactoryHelper = new ExceptionMapper<FROM, TO>();
      problemDetailsFactoryHelper.throwableClass = this.throwableClass;
      problemDetailsFactoryHelper.title = this.title;
      problemDetailsFactoryHelper.instanceSetter = this.instanceSetter;
      problemDetailsFactoryHelper.detailSetter = this.detailSetter;
      problemDetailsFactoryHelper.statusSetter = this.statusSetter;
      problemDetailsFactoryHelper.status = this.status;
      problemDetailsFactoryHelper.titleSetter = this.titleSetter;
      problemDetailsFactoryHelper.typeSetter = this.typeSetter;
      problemDetailsFactoryHelper.additionalAttributeSetter = this.additionalAttributeSetter;
      problemDetailsFactoryHelper.factory = this.factory;
      problemDetailsFactoryHelper.type = this.type;
      return problemDetailsFactoryHelper;
    }
  }
}
