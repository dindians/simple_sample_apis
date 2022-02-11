package org.example.simple_sample_apis.app.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "spring.main.web-application-type=reactive", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class CustomersControllerTest {
  static class MyCustomer {
    private Integer id;
    public Integer getId() { return id; }
  }

  @Autowired
  private WebTestClient webClient;

  @Test
  void getCustomerNotFound() {
    final var customerId = -1;
    webClient.get().uri("/customers/" + customerId)
      .exchange()
      .expectStatus().isNotFound()
      .expectBody(String.class).value(Assertions::assertNotNull);
  }

  @Test
  void getCustomerOk() {
    final var customerId = 13;
    webClient.get().uri("/customers/" + customerId)
      .exchange()
      .expectStatus().isOk()
      .expectBody(String.class).value( customerJson -> {
        assertNotNull(customerJson);
        try {
          var customer = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(customerJson, MyCustomer.class);
          assertAll(
            () -> assertNotNull(customer),
            () -> assertEquals(customerId, customer.id)
          );
        }
        catch (Exception exception) {
          assertNull(exception, String.format("unexpected Exception of type %s", exception.getClass().getName()));
        }
      });
  }
}
