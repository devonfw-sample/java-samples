package com.baumeister.sndbx.exceptionhandling.service;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BookingServiceTest {
  @LocalServerPort
  private int port;
  private final static ObjectMapper mapper = new ObjectMapper();

  private final HttpClient client = HttpClient.newBuilder().build();
  private static final long NOT_FOUND_BOOKING_ID = 404L;

  @Test
  void testNotFoundException () throws IOException, InterruptedException {
    var response = client.send(getBookingHttpRequest(NOT_FOUND_BOOKING_ID), BodyHandlers.ofString());
    var body = response.body();
    var json = """
        {
          "type": "urn:problems:not-found",
          "title": "Resource not found",
          "status": 404,
          "detail": "The element of id 404 could not be found",
          "instance": "%s"
        }
        """;
    json = String.format(json, mapper.readTree(body).at("/instance").asText());
    assertEquals(mapper.readTree(json),mapper.readTree(body));
  }

  private HttpRequest getBookingHttpRequest(long id) {
    String uri = "http://localhost:"+port+"/booking/"+id;
    return HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(uri)).build();
  }

}