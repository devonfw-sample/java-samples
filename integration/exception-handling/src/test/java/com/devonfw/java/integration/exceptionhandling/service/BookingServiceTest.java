package com.devonfw.java.integration.exceptionhandling.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.devonfw.devon4j.generated.api.model.BookingTo;
import com.devonfw.java.integration.exceptionhandling.testdata.to.BookingToBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BookingServiceTest {

  private final static ObjectMapper mapper = new ObjectMapper();
  private static final long NOT_FOUND_BOOKING_ID = 404L;
  private final HttpClient client = HttpClient.newBuilder().build();
  @LocalServerPort
  private int port;

  /**
   * Given a bookingTo is provided to the createBooking endpoint
   * <p>
   * When the bookingTo is using a SeatNumber greater than 20, which is invalid according to the
   * OpenAPI spec
   * <p>
   * Then a ProblemDetail is returned informing about the invalid fields
   */
  @Test
  void testValidationError() throws IOException, InterruptedException {
    final int expectedStatusCode = HttpStatus.SC_NOT_ACCEPTABLE;
    BookingTo booking = BookingToBuilder.aBookingTo()
        .withNumberOfSeats(21)
        .build();
    var response = client.send(createBooking(booking), BodyHandlers.ofString());
    int actualStatusCode = response.statusCode();
    assertEquals(expectedStatusCode, actualStatusCode);
    var body = response.body();
    var json = """
        {
          "_schema":"ValidationProblemDetails",
          "type": "urn:problem:validation-error",
          "title": "A validation failed",
          "status": %d,
          "detail": "Validation failed for 1 fields",
          "instance": "%s",
          "failedValidation": [
              "numberOfSeats"
          ]
        }
        """;
    String actualInstanceUuid = mapper.readTree(body).at("/instance").asText();
    json = String.format(json, expectedStatusCode, actualInstanceUuid);
    assertEquals(mapper.readTree(json), mapper.readTree(body));
  }

  /**
   * When an exception is thrown, it's next mapped superclass should be mapped. This is tested using
   * the{@link com.devonfw.java.integration.exceptionhandling.general.exception.OverBookedException}
   * (not explicitly mapped) and the
   * {@link com.devonfw.java.integration.exceptionhandling.general.exception.BusinessException} that
   * is mapped.
   * <p>
   * Given a booking should be created.
   * <p>
   * When an
   * {@link com.devonfw.java.integration.exceptionhandling.general.exception.OverBookedException} is
   * thrown
   * <p>
   * Then it should be mapped implicitly to a
   * {@link com.devonfw.devon4j.generated.api.model.ProblemDetailsTo} using the abstract
   * {@link com.devonfw.java.integration.exceptionhandling.general.exception.BusinessException}
   */
  @Test
  void testBusinessException() throws IOException, InterruptedException {
    final int expectedStatusCode = HttpStatus.SC_BAD_REQUEST;
    BookingTo booking = BookingToBuilder.aBookingTo()
        .withNumberOfSeats(12)
        .build();
    var response = client.send(createBooking(booking), BodyHandlers.ofString());
    int actualStatusCode = response.statusCode();
    assertEquals(expectedStatusCode, actualStatusCode);
    var body = response.body();
    var json = """
        {
          "_schema":"ProblemDetails",
          "type": "urn:problem:bad-request",
          "title": "Bad Request",
          "status": %d,
          "detail": "Sadly there's no free table at the moment",
          "instance": "%s"
        }
        """;
    String actualInstanceUuid = mapper.readTree(body).at("/instance").asText();
    json = String.format(json, expectedStatusCode, actualInstanceUuid);
    assertEquals(mapper.readTree(json), mapper.readTree(body));
  }

  /**
   * Test the explicit mapping of a business exception. The expectation is that a
   * {@link com.devonfw.java.integration.exceptionhandling.general.exception.NotFoundException}
   * (child of
   * {@link com.devonfw.java.integration.exceptionhandling.general.exception.BusinessException} is
   * thrown and the correct mapper is used to map it to a
   * {@link com.devonfw.devon4j.generated.api.model.ProblemDetailsTo} using the right messages.
   * <p>
   * Given a Booking with id 404 should be returned
   * <p>
   * When this is not available
   * <p>
   * Then a {@link com.devonfw.devon4j.generated.api.model.ProblemDetailsTo} is returned describing
   * the error.
   */
  @Test
  void testNotFoundException() throws IOException, InterruptedException {
    final int expectedStatusCode = HttpStatus.SC_NOT_FOUND;
    var response = client.send(getBookingHttpRequest(NOT_FOUND_BOOKING_ID),
        BodyHandlers.ofString());
    int actualStatusCode = response.statusCode();
    assertEquals(expectedStatusCode, actualStatusCode);
    var body = response.body();
    var json = """
        {
          "_schema":"ProblemDetails",
          "type": "urn:problem:not-found",
          "title": "Resource not found",
          "status": %d,
          "detail": "The element of id 404 could not be found",
          "instance": "%s"
        }
        """;
    String actualInstanceUuid = mapper.readTree(body).at("/instance").asText();
    json = String.format(json, expectedStatusCode, actualInstanceUuid);
    assertEquals(mapper.readTree(json), mapper.readTree(body));
  }

  /**
   * Test the scenario of a not explicitly mapped technical exception.
   * <p>
   * Given a Booking should be returned without an id provided
   * <p>
   * When no booking id is provided
   * <p>
   * Then an exception is thrown and should be mapped to a generic internal server error exception
   * not exposing technical details.
   */
  @Test
  void testInvalidArgumentException() throws IOException, InterruptedException {
    final int expectedStatusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
    var response = client.send(getBookingHttpRequest(null),
        BodyHandlers.ofString());
    int actualStatusCode = response.statusCode();
    assertEquals(expectedStatusCode, actualStatusCode);
    var body = response.body();
    var json = """
        {
          "_schema":"ProblemDetails",
          "type": "urn:problem:internal-server-error",
          "title": "An internal server error occurred",
          "status": %d,
          "detail": "An unexpected error has occurred! We apologize any inconvenience. Please try again later.",
          "instance": "%s"
        }
        """;
    String actualInstanceUuid = mapper.readTree(body).at("/instance").asText();
    json = String.format(json, expectedStatusCode, actualInstanceUuid);
    assertEquals(mapper.readTree(json), mapper.readTree(body));
  }

  private HttpRequest createBooking(BookingTo booking) throws JsonProcessingException {
    String uri = "http://localhost:" + port + "/booking";
    return HttpRequest.newBuilder()
        .POST(BodyPublishers.ofString(mapper.writeValueAsString(booking)))
        .setHeader("Content-Type", ContentType.APPLICATION_JSON.toString())
        .uri(URI.create(uri)).build();
  }

  private HttpRequest getBookingHttpRequest(Long id) {
    String uri = "http://localhost:" + port + "/booking/" + id;
    return HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(uri)).build();
  }

}
