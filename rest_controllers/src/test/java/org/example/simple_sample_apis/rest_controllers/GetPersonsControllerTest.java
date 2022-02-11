package org.example.simple_sample_apis.rest_controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.main.web-application-type=reactive", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetPersonsControllerTest {
  @Autowired
  private WebTestClient webClient;

  static class PersonResponse {
    private Long id;
    public Long getId() { return id; }
  }

  @Test
  void getPersonSucceeds() {
    final var personId = 16L;
    webClient.get().uri("/persons/" + personId)
      .exchange()
      .expectStatus().isOk()
      .expectBody(String.class).value(customerJson -> {
        assertAll(
          () -> assertNotNull(customerJson),
          () -> assertTrue(customerJson.contains(personId + ""))
        );
        try {
          var personResponse = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(customerJson, PersonResponse.class);
          assertAll(
            () -> assertNotNull(personResponse),
            () -> assertEquals(personId, personResponse.id)
          );
        }
        catch (Exception exception) {
          assertNull(exception, String.format("unexpected Exception of type %s", exception.getClass().getName()));
        }
      });
  }
}
