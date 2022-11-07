package com.devonfw.java.integration.exceptionhandling.service.exception;

import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * This Mapper Class is a high customizable implementation, allowing to define custom mappings from
 * any exception to any ProblemDetails derivation.
 * <p>
 * According to RFC 7807 a ProblemDetail consists at least of the fields:
 * <ul>
 *   <li>type</li>
 *   <li>title</li>
 *   <li>status</li>
 *   <li>instance</li>
 *   <li>detail</li>
 * </ul>
 * If necessary additional fields can be added.
 * <p>
 * For each of the fields this MapperClass contains a Lambda Consumer.
 * This consumer takes the incoming exception and a problemDetail.
 * It defines a function how to set the depending field in the problemDetail.
 * The implementation might use the fields from the exception.
 * <p>
 * For example the detailSetter might use the message from the exception and set it to the detail field
 * in the problemDetails.
 * <p>
 * <p>
 * For each exception that needs a mapping an own ExceptionMapper instance should be created.
 * This should define how to map the given exception to the according ProblemDetails derivation.
 * <p>
 * An inner builder is available to easily create ExceptionMappers.
 * This builder defines certain defaults that make it easy to only specify the additional mapping definitions
 * <ul>
 *   <li>A type can be set directly as variable and will be included in the problemDetails</li>
 *   <li>A title can be set directly as variable and will be included in the problemDetails</li>
 *   <li>A status can be set directly as variable and will be included in the problemDetails</li>
 *   <li>The instance creates a new random UUID</li>
 *   <li>The detail field will be set from the exception message</li>
 *   <li>Additional fields are not set at all</li>
 * </ul>
 *
 * @param <FROM> Mapping from the given {@link Throwable}
 * @param <TO>   To the given {@link ProblemDetailsTo}
 */
public class ExceptionMapper<FROM extends Throwable, TO extends ProblemDetailsTo> {

  /**
   * Stores the class of the throwable, this way an easy mapping is possible when searching for the
   * right mapper
   */
  private Class<FROM> throwableClass;

  /**
   * A factory supplier is necessary to create the {@link ProblemDetailsTo} class. The factory is
   * mandatory as it cannot be created otherwise at runtime (except for reflection, which we wanted
   * to avoid here) A factory is usual the {@link ProblemDetailsTo} derivation added by `::new` for
   * example: `ValidationProblemDetailsTo::new`
   */
  private Supplier<TO> factory;
  private BiConsumer<FROM, TO> typeSetter;
  private BiConsumer<FROM, TO> titleSetter;
  private BiConsumer<FROM, TO> instanceSetter;
  private BiConsumer<FROM, TO> statusSetter;
  private BiConsumer<FROM, TO> detailSetter;
  private BiConsumer<FROM, TO> additionalAttributeSetter;

  /**
   * Maps the exception to the {@link ProblemDetailsTo}.
   *
   * @param ex The exception that was thrown.
   * @return A new {@link ProblemDetailsTo} (or inherited) instance
   */
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

  public Class<FROM> getThrowableClass() {
    return throwableClass;
  }

  public static final class ExceptionMapperBuilder<FROM extends Throwable, TO extends ProblemDetailsTo> {

    private final Class<FROM> throwableClass;

    private final Supplier<TO> factory;
    private String type = "urn:problem:internal-server-error";
    private String title = "An internal server error occurred";
    private Integer status = 500;
    private BiConsumer<FROM, TO> typeSetter;
    private BiConsumer<FROM, TO> titleSetter;
    private BiConsumer<FROM, TO> instanceSetter;
    private BiConsumer<FROM, TO> statusSetter;
    private BiConsumer<FROM, TO> detailSetter;
    private BiConsumer<FROM, TO> additionalAttributeSetter;


    public ExceptionMapperBuilder(Class<FROM> throwableClass, Supplier<TO> problemDetailsFactory) {
      this.factory = problemDetailsFactory;
      this.throwableClass = throwableClass;
    }

    /**
     * Create a Builder.
     *
     * @param throwableClass        The Class of the {@link Throwable} that needs to be mapped.
     * @param problemDetailsFactory A Supplier that creates a new instance of the
     *                              {@link ProblemDetailsTo} or an inherited class. This supplier
     *                              usually is the constructor call of the Class.
     * @param <E>                   The concrete {@link Throwable}
     * @param <P>                   The concrete {@link ProblemDetailsTo}
     * @return A new builder instance
     */
    public static <E extends Throwable, P extends ProblemDetailsTo> ExceptionMapperBuilder<E, P> builder(
        Class<E> throwableClass, Supplier<P> problemDetailsFactory) {
      return new ExceptionMapperBuilder<E, P>(throwableClass, problemDetailsFactory);
    }


    /**
     * A comfort function that allows to easily change the type without defining a setter. A default
     * setter uses this type field to set it in the problemDetails.
     *
     * @param type The type that wil be written into the ProblemDetails
     * @return ExceptionMapperBuilder for chaining.
     */
    public ExceptionMapperBuilder<FROM, TO> withType(String type) {
      this.type = type;
      return this;
    }

    /**
     * A comfort function that allows to easily change the title without defining a setter. A
     * default setter uses this title field to set it in the problemDetails. The title should
     * describe the type in a human understandable manner in a static manner. The title should not
     * name details that are specific for the instance of the error (like the id of a resource)
     *
     * @param title The title that wil be written into the ProblemDetails
     * @return ExceptionMapperBuilder for chaining.
     */
    public ExceptionMapperBuilder<FROM, TO> withTitle(String title) {
      this.title = title;
      return this;
    }

    /**
     * A comfort function that allows to easily change the status without defining a setter. A
     * default setter uses this status field to set it in the problemDetails. The status is the Http
     * Status of the response.
     *
     * @param status The status that wil be written into the ProblemDetails
     * @return ExceptionMapperBuilder for chaining.
     */
    public ExceptionMapperBuilder<FROM, TO> withStatus(Integer status) {
      this.status = status;
      return this;
    }

    /**
     * Define how to set types in the problemDetails. This lambda BiConsumer takes the original
     * throwable and the newly created problemDetails. The problemDetails type field can then be set
     * with static values or with information from the throwable.
     *
     * <pre>
     *   ...
     *   .withTypeSetter((throwable, problemDetails) -> {problemDetails.setType("urn:problem:not-found")})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withTypeSetter(BiConsumer<FROM, TO> typeSetter) {
      this.typeSetter = typeSetter;
      return this;
    }

    /**
     * Define how to set title in the problemDetails. This lambda BiConsumer takes the original
     * throwable and the newly created problemDetails. The problemDetails title field can then be
     * set with static values or with information from the throwable.
     *
     * <pre>
     *   ...
     *   .withTitleSetter((throwable, problemDetails) -> {problemDetails.setTitle("Resource not found")})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withTitleSetter(BiConsumer<FROM, TO> titleSetter) {
      this.titleSetter = titleSetter;
      return this;
    }

    /**
     * Define how to set the instance in the problemDetails. This lambda BiConsumer takes the
     * original throwable and the newly created problemDetails. The problemDetails instance field
     * can then be set with static values or with information from the throwable.
     * <p>
     * As a default, a random UUID is generated. But it could also be used to map to the correlation
     * id. Or anything that identifies the error instance.
     *
     * <pre>
     *   ...
     *    // Use the correlation id as instance
     *   .withInstanceSetter((throwable, problemDetails) -> {problemDetails.setInstance(MDC.get("correlationId"))})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withInstanceSetter(
        BiConsumer<FROM, TO> instanceSetter) {
      this.instanceSetter = instanceSetter;
      return this;
    }

    /**
     * Define how to set the status in the problemDetails. This lambda BiConsumer takes the original
     * throwable and the newly created problemDetails. The problemDetails status field can then be
     * set with static values or with information from the throwable.
     *
     * <pre>
     *   ...
     *   .withStatusSetter((throwable, problemDetails) -> {problemDetails.setStatus(404)})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withStatusSetter(BiConsumer<FROM, TO> statusSetter) {
      this.statusSetter = statusSetter;
      return this;
    }

    /**
     * Define how to set the detail in the problemDetails. This lambda BiConsumer takes the original
     * throwable and the newly created problemDetails. The problemDetails detail field can then be
     * set with static values or with information from the throwable.
     *
     * <pre>
     *   ...
     *   .withDetailSetter((throwable, problemDetails) -> {problemDetails.setDetail(ex.getMessage())})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withDetailSetter(BiConsumer<FROM, TO> detailSetter) {
      this.detailSetter = detailSetter;
      return this;
    }

    /**
     * Define how to set the additionalFields in the problemDetails. This lambda BiConsumer takes
     * the original throwable and the newly created problemDetails. The problemDetails
     * additionalFields field can then be set with static values or with information from the
     * throwable.
     * <p>
     * By default, those are empty. The inherited class of problemDetailsTo needs to have the
     * additionalFields. Like e.g.
     * {@link com.devonfw.devon4j.generated.api.model.ValidationProblemDetailsTo} contains an String
     * array for further details of the failed validation.
     *
     * <pre>
     *   ...
     *   // PseudoCode! the Validation Throwable does not necessarily have an easy usage of the reason list.
     *   // That might need more implementation!
     *   .withAdditionalAttributeSetter((throwable, problemDetails) -> {problemDetails.setFailedValidations(ex.getListOfValidationErrors())})
     *   .build()
     * </pre>
     */
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
      problemDetailsFactoryHelper.factory = this.factory;
      problemDetailsFactoryHelper.throwableClass = this.throwableClass;
      problemDetailsFactoryHelper.instanceSetter = this.instanceSetter;
      if (this.instanceSetter == null) {
        problemDetailsFactoryHelper.instanceSetter = (ex, problem) -> problem.setInstance(
            "urn:uuid:" + UUID.randomUUID());
      }
      problemDetailsFactoryHelper.detailSetter = this.detailSetter;
      if (this.detailSetter == null) {
        problemDetailsFactoryHelper.detailSetter = (ex, problem) -> problem.setDetail(
            ex.getMessage());
      }
      problemDetailsFactoryHelper.statusSetter = this.statusSetter;
      if (this.statusSetter == null) {
        problemDetailsFactoryHelper.statusSetter = (ex, problem) -> problem.setStatus(this.status);
      }
      problemDetailsFactoryHelper.titleSetter = this.titleSetter;
      if (this.titleSetter == null) {
        problemDetailsFactoryHelper.titleSetter = (ex, problem) -> problem.setTitle(this.title);
      }
      problemDetailsFactoryHelper.typeSetter = this.typeSetter;
      if (this.typeSetter == null) {
        problemDetailsFactoryHelper.typeSetter = (ex, problem) -> problem.setType(this.type);
      }
      problemDetailsFactoryHelper.additionalAttributeSetter = this.additionalAttributeSetter;
      return problemDetailsFactoryHelper;
    }
  }
}
