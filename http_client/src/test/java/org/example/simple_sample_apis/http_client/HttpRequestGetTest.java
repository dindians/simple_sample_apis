package org.example.simple_sample_apis.http_client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import java.net.UnknownHostException;
import static org.junit.jupiter.api.Assertions.*;

final class HttpRequestGetTest {
  static class AstronomicPicture {
    private String date;
    private String explanation;
    private String media_type;
    private String title;
    private String url;

    public String getDate() { return date; }
    public String getExplanation() { return explanation; }
    public String getMedia_type() { return media_type; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
  }

  @Test
  void getRequestWithResponseOk() {
    StepVerifier
      .create(HttpRequest.from("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY").get())
      .assertNext(httpResponse -> {
        assertAll(
          () -> assertNotNull(httpResponse),
          () -> assertTrue(httpResponse.isSuccess()),
          () -> assertNotNull(httpResponse.asSuccess()),
          () -> assertNull(httpResponse.asFailure()),
          () -> assertEquals(200, httpResponse.asSuccess().getStatusCode())
        );
        var httpResponseSuccess = httpResponse.asSuccess();
        assertAll(
          () -> assertNotNull(httpResponseSuccess),
          () -> assertEquals(200, httpResponseSuccess.getStatusCode()),
          () -> assertNotNull(httpResponseSuccess.getBody())
        );
        try {
          final var astronomicPicture = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(httpResponseSuccess.getBody(), AstronomicPicture.class);
          assertAll(
            () -> assertNotNull(astronomicPicture, "picture is null"),
            () -> assertNotNull(astronomicPicture.getDate(), "Date field is null"),
            () -> assertNotNull(astronomicPicture.getExplanation(), "Explanation field is null"),
            () -> assertNotNull(astronomicPicture.getMedia_type(), "MediaType field is null"),
            () -> assertNotNull(astronomicPicture.getTitle(), "Title field is null"),
            () -> assertNotNull(astronomicPicture.getUrl(), "Url field is null")
          );
        } catch (JsonProcessingException e) {
          assertNull(true, "the following json mapper exception occurred\n" + e);
          e.printStackTrace();
        }
      })
      .expectComplete()
      .log()
      .verify();
  }

  @Test
  void getRequestWithResponseNotFound() {
    StepVerifier
      .create(HttpRequest.from("https://api.nasa.gov/planetary/apod-not-existing?api_key=DEMO_KEY").get())
      .assertNext(httpResponse -> assertAll(
        () -> assertNotNull(httpResponse),
        () -> assertTrue(httpResponse.isSuccess()),
        () -> assertNotNull(httpResponse.asSuccess()),
        () -> assertNull(httpResponse.asFailure()),
        () -> assertEquals(404, httpResponse.asSuccess().getStatusCode())
      ))
      .expectComplete()
      .log()
      .verify();
  }

  @Test
  void getRequestWithResponseForbidden() {
    StepVerifier
      .create(HttpRequest.from("https://api.nasa.gov/planetary/apod").get())
      .assertNext(httpResponse -> assertAll(
        () -> assertNotNull(httpResponse),
        () -> assertTrue(httpResponse.isSuccess()),
        () -> assertNotNull(httpResponse.asSuccess()),
        () -> assertNull(httpResponse.asFailure()),
        () -> assertEquals(403, httpResponse.asSuccess().getStatusCode())
      ))
      .expectComplete()
      .log()
      .verify();
  }

  @Test
  void getRequestWithResponseFailure() {
    final var unknownHost = "api.nasa.gov-not-existing";
    StepVerifier
      .create(HttpRequest.from("https://" + unknownHost + "/planetary/apod").get())
      .assertNext(httpResponse -> {
        assertAll(
          () -> assertNotNull(httpResponse),
          () -> assertFalse(httpResponse.isSuccess()),
          () -> assertNull(httpResponse.asSuccess()),
          () -> assertNotNull(httpResponse.asFailure())
        );
        final var httpResponseFailure = httpResponse.asFailure();
        assertAll(
          () -> assertNotNull(httpResponseFailure, "httpResponseFailure not null"),
          () -> assertNotNull(httpResponseFailure.getThrowable(), "httpResponseFailure.getThrowable() not null"),
          () -> assertTrue(httpResponseFailure.getThrowable().getMessage().startsWith(String.format("Failed to resolve '%s'", unknownHost)), "httpResponseFailure.getThrowable().getMessage().contains..."),
          () -> assertNotNull(httpResponseFailure.getThrowable().getCause(), "httpResponseFailure.getThrowable().getCause() not null"),
          () -> assertTrue(httpResponseFailure.getThrowable().getCause() instanceof UnknownHostException, "httpResponseFailure.getThrowable().getCause() instanceof UnknownHostException")
        );
      })
      .verifyComplete();
  }
}
