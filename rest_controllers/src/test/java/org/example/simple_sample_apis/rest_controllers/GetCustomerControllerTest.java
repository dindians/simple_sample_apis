package org.example.simple_sample_apis.rest_controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.main.web-application-type=reactive", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetCustomerControllerTest {
  @Autowired
  private WebTestClient webClient;

  static class ResponseCustomer {
    private Integer id;
    public Integer getId() { return id; }
  }


  @Test
  void getCustomerSucceeds() {
    final var customerId = 13;
    webClient.get().uri("/customers/" + customerId)
      .exchange()
      .expectStatus().isOk()
      .expectBody(String.class).value(customerJson -> {
        assertAll(
          () -> assertNotNull(customerJson),
          () -> assertTrue(customerJson.contains(customerId + ""))
        );
        try {
          var responseCustomer = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(customerJson, ResponseCustomer.class);
          assertAll(
            () -> assertNotNull(responseCustomer),
            () -> assertEquals(customerId, responseCustomer.id)
          );
        }
        catch (Exception exception) {
          assertNull(exception, String.format("unexpected Exception of type %s", exception.getClass().getName()));
        }
      });
  }
}
