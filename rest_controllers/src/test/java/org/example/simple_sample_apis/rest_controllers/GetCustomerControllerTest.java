package org.example.simple_sample_apis.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.main.web-application-type=reactive", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetCustomerControllerTest {
  @Autowired
  private WebTestClient webClient;

  @Test
  void getCustomerSucceeds() {
    final var customerId = 13;
    webClient.get().uri("/customers/" + customerId)
      .exchange()
      .expectStatus().isOk()
      .expectBody(String.class).value(customerJson ->
        assertAll(
          () -> assertNotNull(customerJson),
          () -> assertTrue(customerJson.contains(customerId + ""))
        ));
  }
}
