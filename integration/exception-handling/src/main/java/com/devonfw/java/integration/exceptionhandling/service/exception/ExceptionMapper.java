package com.devonfw.java.integration.exceptionhandling.service.exception;

import com.devonfw.devon4j.generated.api.model.ProblemDetailsTo;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
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
 * For each of the fields this MapperClass contains a Lambda Function.
 * This consumer takes the incoming exception and a problemDetail.
 * It defines a function how to set the corresponding field in the problemDetail.
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
  private Function<FROM, String> type;
  private Function<FROM, String> title;
  private Function<FROM, String> instance;
  private Function<FROM, Integer> status;
  private Function<FROM, String> details;

  /**
   * Additional fields are one or more attributes on the specialized class inherited from
   * ProblemDetailsTo Therefore, a BICOnsumer is necessary. It's expected that the BiConsumer sets
   * the corresponding field in the ProblemDetailsTo directly
   *
   * <pre>
   *   additionalFieldsSetter = (ex, problem) -> {
   *     problem.setFailedValidations(ex.getFailedValidationList);
   *     problem.setFurtherField(ex.furtherInformation);
   *    }
   * </pre>
   */
  private BiConsumer<FROM, TO> additionalAttributeSetter;

  /**
   * Maps the exception to the {@link ProblemDetailsTo}.
   *
   * @param ex The exception that was thrown.
   * @return A new {@link ProblemDetailsTo} (or inherited) instance
   */
  public TO map(FROM ex) {

    TO problemDetails = this.factory.get();
    problemDetails.setType(type.apply(ex));
    problemDetails.setTitle(title.apply(ex));
    problemDetails.setDetail(details.apply(ex));
    problemDetails.setStatus(status.apply(ex));
    problemDetails.setInstance(instance.apply(ex));

    if (this.additionalAttributeSetter != null) {
      // Here the additionalFields are set on the problemDetails
      // It can be argued that this is a code smell, because the lambda changes the
      // parameter as call by reference. Sadly there's no generic way to handle this, because the
      // attributes in problemDetails depend on the inherited class and are not known here.
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
    private Function<FROM, String> typeDefinition;
    private Function<FROM, String> titleDefinition;
    private Function<FROM, String> instanceDefinition;
    private Function<FROM, Integer> statusDefinition;
    private Function<FROM, String> detailDefinition;
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
     *   .withTypeDefinition((throwable, problemDetails) -> {problemDetails.setType("urn:problem:not-found")})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withTypeDefinition(Function<FROM, String> typeDefinition) {
      this.typeDefinition = typeDefinition;
      return this;
    }

    /**
     * Define how to set title in the problemDetails. This lambda BiConsumer takes the original
     * throwable and the newly created problemDetails. The problemDetails title field can then be
     * set with static values or with information from the throwable.
     *
     * <pre>
     *   ...
     *   .withTitleDefinition((throwable, problemDetails) -> {problemDetails.setTitle("Resource not found")})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withTitleDefinition(Function<FROM, String> titleDefinition) {
      this.titleDefinition = titleDefinition;
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
     *   .withInstanceDefinition((throwable, problemDetails) -> {problemDetails.setInstance(MDC.get("correlationId"))})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withInstanceDefinition(
        Function<FROM, String> instanceDefinition) {
      this.instanceDefinition = instanceDefinition;
      return this;
    }

    /**
     * Define how to set the status in the problemDetails. This lambda BiConsumer takes the original
     * throwable and the newly created problemDetails. The problemDetails status field can then be
     * set with static values or with information from the throwable.
     *
     * <pre>
     *   ...
     *   .withStatusDefinition((throwable, problemDetails) -> {problemDetails.setStatus(404)})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withStatusDefinition(Function<FROM, Integer> statusDefinition) {
      this.statusDefinition = statusDefinition;
      return this;
    }

    /**
     * Define how to set the detail in the problemDetails. This lambda BiConsumer takes the original
     * throwable and the newly created problemDetails. The problemDetails detail field can then be
     * set with static values or with information from the throwable.
     *
     * <pre>
     *   ...
     *   .withDetailDefinition((throwable, problemDetails) -> {problemDetails.setDetail(ex.getMessage())})
     *   .build()
     * </pre>
     */
    public ExceptionMapperBuilder<FROM, TO> withDetailDefinition(Function<FROM, String> detailDefinition) {
      this.detailDefinition = detailDefinition;
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
      ExceptionMapper<FROM, TO> problemDetailsFactoryHelper = new ExceptionMapper<>();
      problemDetailsFactoryHelper.factory = this.factory;
      problemDetailsFactoryHelper.throwableClass = this.throwableClass;
      problemDetailsFactoryHelper.instance = this.instanceDefinition;
      if (this.instanceDefinition == null) {
        problemDetailsFactoryHelper.instance = (ex) -> "urn:uuid:" + UUID.randomUUID();
      }
      problemDetailsFactoryHelper.details = this.detailDefinition;
      if (this.detailDefinition == null) {
        problemDetailsFactoryHelper.details = Throwable::getMessage;
      }
      problemDetailsFactoryHelper.status = this.statusDefinition;
      if (this.statusDefinition == null) {
        problemDetailsFactoryHelper.status = (ex) -> this.status;
      }
      problemDetailsFactoryHelper.title = this.titleDefinition;
      if (this.titleDefinition == null) {
        problemDetailsFactoryHelper.title = (ex) -> this.title;
      }
      problemDetailsFactoryHelper.type = this.typeDefinition;
      if (this.typeDefinition == null) {
        problemDetailsFactoryHelper.type = (ex) -> this.type;
      }
      problemDetailsFactoryHelper.additionalAttributeSetter = this.additionalAttributeSetter;
      return problemDetailsFactoryHelper;
    }
  }
}
