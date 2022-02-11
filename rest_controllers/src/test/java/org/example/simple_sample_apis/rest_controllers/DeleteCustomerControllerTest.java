package org.example.simple_sample_apis.rest_controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(properties = "spring.main.web-application-type=reactive", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteCustomerControllerTest {
  @Autowired
  private WebTestClient webClient;

  @Test
  void SuccessfullyDeleteCustomer() {
    final var customerId = 1;
    webClient.delete().uri("/customers/" + customerId)
      .exchange()
      .expectStatus().isNoContent()
      .expectBody(String.class).value(Assertions::assertNull);
  }
}
