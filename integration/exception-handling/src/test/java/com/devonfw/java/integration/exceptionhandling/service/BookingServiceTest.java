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
   * Given a bookingTo is provided to the createBooking endpoint When the bookingTo is using a
   * SeatNumber greater than 20, which is invalid according to the OpenAPI spec Then a ProblemDetail
   * is returned informing about the invalid fields
   */
  @Test
  void validationError() throws IOException, InterruptedException {
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

  @Test
  void testNotFoundException() throws IOException, InterruptedException {
    var response = client.send(getBookingHttpRequest(NOT_FOUND_BOOKING_ID),
        BodyHandlers.ofString());
    var body = response.body();
    var json = """
        {
          "_schema":"ProblemDetails",
          "type": "urn:problem:not-found",
          "title": "Resource not found",
          "status": 404,
          "detail": "The element of id 404 could not be found",
          "instance": "%s"
        }
        """;
    json = String.format(json, mapper.readTree(body).at("/instance").asText());
    assertEquals(mapper.readTree(json), mapper.readTree(body));
  }

  private HttpRequest createBooking(BookingTo booking) throws JsonProcessingException {
    String uri = "http://localhost:" + port + "/booking";
    return HttpRequest.newBuilder()
        .POST(BodyPublishers.ofString(mapper.writeValueAsString(booking)))
        .setHeader("Content-Type", ContentType.APPLICATION_JSON.toString())
        .uri(URI.create(uri)).build();
  }

  private HttpRequest getBookingHttpRequest(long id) {
    String uri = "http://localhost:" + port + "/booking/" + id;
    return HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(uri)).build();
  }

}
