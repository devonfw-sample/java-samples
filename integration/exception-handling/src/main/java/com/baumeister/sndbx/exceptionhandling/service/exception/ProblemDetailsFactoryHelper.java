package com.baumeister.sndbx.exceptionhandling.service.exception;

import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ProblemDetailsFactoryHelper<E extends Throwable, P extends ProblemDetailsTo> {

  private Supplier<P> factory;
  private String type = "urn:problem:internal-server-error";
  private String title = "Internal Server Error";
  private Integer status = 500;
  private BiConsumer<E, P> typeSetter = (ex, problem) -> problem.setType(this.type);
  private BiConsumer<E, P> titleSetter = (ex, problem) -> problem.setTitle(this.title);
  private BiConsumer<E, P> instanceSetter = (ex, problem) -> problem.setInstance(
      UUID.randomUUID().toString());
  private BiConsumer<E, P> statusSetter = (ex, problem) -> problem.setStatus(this.status);
  private BiConsumer<E, P> detailSetter = (ex, problem) -> problem.setDetail(ex.getMessage());
  private BiConsumer<E, P> additionalAttributeSetter;

  public Supplier<P> getFactory() {
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

  public  BiConsumer<E, P> getTypeSetter() {
    return typeSetter;
  }

  public BiConsumer<E, P> getTitleSetter() {
    return titleSetter;
  }

  public BiConsumer<E, P> getInstanceSetter() {
    return instanceSetter;
  }

  public BiConsumer<E, P> getStatusSetter() {
    return statusSetter;
  }

  public BiConsumer<E, P> getDetailSetter() {
    return detailSetter;
  }

  public BiConsumer<E, P> getAdditionalAttributeSetter() {
    return additionalAttributeSetter;
  }

  public static final class ProblemDetailsFactoryHelperBuilder<E extends Throwable, P extends ProblemDetailsTo> {

    private Supplier<P> factory;
    private String type = "urn:problem:internal-server-error";
    private String title = "Internal Server Error";
    private Integer status = 500;
    private BiConsumer<E, P> typeSetter = (ex, problem) -> problem.setType(this.type);
    private BiConsumer<E, P> titleSetter = (ex, problem) -> problem.setTitle(this.title);
    private BiConsumer<E, P> instanceSetter = (ex, problem) -> problem.setInstance(
        "urn:uuid:" + UUID.randomUUID().toString());
    private BiConsumer<E, P> statusSetter = (ex, problem) -> problem.setStatus(this.status);
    private BiConsumer<E, P> detailSetter = (ex, problem) -> problem.setDetail(ex.getMessage());
    private BiConsumer<E, P> additionalAttributeSetter;

    private ProblemDetailsFactoryHelperBuilder() {
    }

    public static <E extends Throwable, P extends ProblemDetailsTo> ProblemDetailsFactoryHelperBuilder<E, P> aProblemDetailsFactoryHelper(
        Class<E> throwableClass, Class<P> problemdetailsClazz) {
      return new ProblemDetailsFactoryHelperBuilder<E, P>();
    }

    public ProblemDetailsFactoryHelperBuilder<E, P> withFactory(Supplier<P> factory) {
      this.factory = factory;
      return this;
    }

    public ProblemDetailsFactoryHelperBuilder<E, P> withType(String type) {
      this.type = type;
      return this;
    }

    public ProblemDetailsFactoryHelperBuilder<E, P> withTitle(String title) {
      this.title = title;
      return this;
    }

    public ProblemDetailsFactoryHelperBuilder<E, P> withStatus(Integer status) {
      this.status = status;
      return this;
    }

    public ProblemDetailsFactoryHelperBuilder withTypeSetter(BiConsumer<E, P> typeSetter) {
      this.typeSetter = typeSetter;
      return this;
    }

    public ProblemDetailsFactoryHelperBuilder withTitleSetter(BiConsumer<E, P> titleSetter) {
      this.titleSetter = titleSetter;
      return this;
    }

    public ProblemDetailsFactoryHelperBuilder withInstanceSetter(BiConsumer<E, P> instanceSetter) {
      this.instanceSetter = instanceSetter;
      return this;
    }

    public ProblemDetailsFactoryHelperBuilder withStatusSetter(BiConsumer<E, P> statusSetter) {
      this.statusSetter = statusSetter;
      return this;
    }

    public ProblemDetailsFactoryHelperBuilder withDetailSetter(BiConsumer<E, P> detailSetter) {
      this.detailSetter = detailSetter;
      return this;
    }

    public ProblemDetailsFactoryHelperBuilder withAdditionalAttributeSetter(
        BiConsumer<E, P> additionalAttributeSetter) {
      this.additionalAttributeSetter = additionalAttributeSetter;
      return this;
    }

    public ProblemDetailsFactoryHelper<E, P> build() {
      ProblemDetailsFactoryHelper<E, P> problemDetailsFactoryHelper = new ProblemDetailsFactoryHelper<E, P>();
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
