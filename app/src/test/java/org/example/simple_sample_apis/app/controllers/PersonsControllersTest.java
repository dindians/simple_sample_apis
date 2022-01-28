package org.example.simple_sample_apis.app.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
public class PersonsControllersTest {
  static class MyPerson {
    private long id;
    public long getId() {
      return id;
    }
  }

  static class PersonError {
    static class Error {
      static class Cause {
        static class PersonMappingError {
          private Integer personId;
          private String errorText;

          public Integer getPersonId() { return personId; }
          public String getErrorText() { return errorText; }
        }
        private PersonMappingError personMappingError;

        public Cause.PersonMappingError getPersonMappingError() { return personMappingError; }
      }
      private String context;
      private String type;
      public Cause cause;

      public String getContext() { return context; }
      public String getType() { return type; }
      public Cause getCause() { return cause; }
    }
    private Error error;

    public Error getError() { return error; }
  }

  @Autowired
  private WebTestClient webClient;

  @Test
  void getPersonOk() {
    final var personId = 14;
    this.webClient.get().uri("/persons/" + personId)
      .exchange()
      .expectStatus().isOk()
      .expectBody(String.class).value(personJson -> {
        assertNotNull(personJson);
        try {
          var person = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(personJson, MyPerson.class);
          assertAll(
            () -> assertNotNull(person),
            () -> assertEquals(personId, person.getId())
          );
        }
        catch (Exception exception) {
          assertNull(exception, String.format("unexpected Exception of type %s", exception.getClass().getName()));
        }
      });
  }

  @Test
  void getPersonFailure() {
    final var personId = 13;
    this.webClient.get().uri("/persons/" + personId)
      .exchange()
      .expectStatus().is5xxServerError()
      .expectBody(String.class).value(errorJson -> {
        assertNotNull(errorJson);
        try {
          var personError = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(errorJson, PersonError.class);
          assertAll(
            () -> assertNotNull(personError),
            () -> assertNotNull(personError.getError()),
            () -> assertNotNull(personError.getError().getCause()),
            () -> assertNotNull(personError.getError().getCause().getPersonMappingError()),
            () -> assertEquals(personId, personError.getError().getCause().getPersonMappingError().getPersonId())
          );
        }
        catch (Exception exception) {
          assertNull(exception, String.format("unexpected Exception of type %s", exception.getClass().getName()));
        }
      });
  }
}
