package org.example.simple_sample_apis.http_client;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class HttpResponseSuccessIsOkTest {
  @Test
  void HttpResponseSuccessIsOk() {
    assertAll(
      () -> assertTrue(HttpResponseSuccess.isOk(200)),
      () -> assertTrue(HttpResponseSuccess.isOk(201)),
      () -> assertTrue(HttpResponseSuccess.isOk(202)),
      () -> assertTrue(HttpResponseSuccess.isOk(204)),
      () -> assertFalse(HttpResponseSuccess.isOk(300)),
      () -> assertFalse(HttpResponseSuccess.isOk(301)),
      () -> assertFalse(HttpResponseSuccess.isOk(302)),
      () -> assertFalse(HttpResponseSuccess.isOk(400)),
      () -> assertFalse(HttpResponseSuccess.isOk(401)),
      () -> assertFalse(HttpResponseSuccess.isOk(403)),
      () -> assertFalse(HttpResponseSuccess.isOk(404)),
      () -> assertFalse(HttpResponseSuccess.isOk(406)),
      () -> assertFalse(HttpResponseSuccess.isOk(500))
    );
  }
}
