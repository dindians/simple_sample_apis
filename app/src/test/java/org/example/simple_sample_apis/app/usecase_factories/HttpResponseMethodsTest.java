package org.example.simple_sample_apis.app.usecase_factories;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.http_client.*;
import org.example.simple_sample_apis.app.*;

class HttpResponseMethodsTest {
  @Test
  void getBodyForHttpResponseSuccessOk() {
    class ExpectedHttpResponseSuccessOk implements HttpResponseSuccess {
      @Override
      public Boolean isSuccess() { return true; }
      @Override
      public HttpResponseSuccess asSuccess() { return this; }
      @Override
      public HttpResponseFailure asFailure() { return null; }
      @Override
      public String getUri() { return "someuri"; }
      @Override
      public Integer getStatusCode() { return 200; }
      @Override
      public String getBody() { return "somebody"; }
    }
    final var expectedHttpResponseSuccessOk = new ExpectedHttpResponseSuccessOk();
    final var eitherErrorOrBody = HttpResponseMethods.getBody(expectedHttpResponseSuccessOk);
    assertAll(
      () -> assertTrue(HttpResponseSuccess.isOk(expectedHttpResponseSuccessOk.getStatusCode())),
      () -> assertNotNull(eitherErrorOrBody),
      () -> assertTrue(eitherErrorOrBody instanceof Either.Right)
    );
    eitherErrorOrBody.map(body -> {
      assertAll(
        () -> assertNotNull(body),
        () -> assertEquals(expectedHttpResponseSuccessOk.getBody(), body)
      );
      return body;
    });
    eitherErrorOrBody.fold(
      usecaseError -> {
        assertAll(
          () -> assertNull(usecaseError)
        );
        return true;
      },
      body -> true
    );
  }

  @Test
  void getBodyForHttpResponseSuccessNotOk() {
    class ExpectedHttpResponseSuccessNotOk implements HttpResponseSuccess {
      @Override
      public Boolean isSuccess() { return true; }
      @Override
      public HttpResponseSuccess asSuccess() { return this; }
      @Override
      public HttpResponseFailure asFailure() { return null; }
      @Override
      public String getUri() { return "some-uri"; }
      @Override
      public Integer getStatusCode() { return 404; }
      @Override
      public String getBody() { return null; }
    }
    final var expectedHttpResponseSuccessNotOk = new ExpectedHttpResponseSuccessNotOk();
    final var eitherErrorOrBody = HttpResponseMethods.getBody(expectedHttpResponseSuccessNotOk);
    assertAll(
      () -> assertFalse(HttpResponseSuccess.isOk(expectedHttpResponseSuccessNotOk.getStatusCode())),
      () -> assertNotNull(eitherErrorOrBody),
      () -> assertTrue(eitherErrorOrBody instanceof Either.Left)
    );
    eitherErrorOrBody.fold(
      usecaseError -> {
        assertAll(
          () -> assertNotNull(usecaseError),
          () -> assertTrue(usecaseError instanceof HttpResponseNotOk)
        );
        final var httpResponseNotOk = (HttpResponseNotOk)usecaseError;
        assertAll(
          () -> assertNotNull(httpResponseNotOk),
          () -> assertNotNull(httpResponseNotOk.getHttpStatusCode())
        );
        return true;
      },
      body -> {
        assertNull(body);
        return true;
      }
    );
  }

  @Test
  void getBodyForHttpResponseFailure() {
    class ExpectedException extends Exception {}
    final ExpectedException expectedException = new ExpectedException();
    class ExpectedHttpResponseFailure implements HttpResponseFailure {
      @Override
      public String getUri() { return "some-uri"; }
      @Override
      public Boolean isSuccess() { return false; }
      @Override
      public HttpResponseSuccess asSuccess() { return null; }
      @Override
      public HttpResponseFailure asFailure() { return this; }
      @Override
      public Throwable getThrowable() { return expectedException; }
    }
    final var expectedHttpResponseFailure = new ExpectedHttpResponseFailure();
    final var eitherErrorOrBody = HttpResponseMethods.getBody(expectedHttpResponseFailure);
    assertAll(
      () -> assertNotNull(eitherErrorOrBody),
      () -> assertTrue(eitherErrorOrBody instanceof Either.Left)
    );
    eitherErrorOrBody.fold(
      usecaseError -> {
        assertAll(
          () -> assertNotNull(usecaseError),
          () -> assertTrue(usecaseError instanceof HttpResponseError)
        );
        final var httpResponseError = (HttpResponseError)usecaseError;
        assertAll(
          () -> assertNotNull(httpResponseError),
          () -> assertNotNull(httpResponseError.getHttpResponseFailure())
        );
        final var httpResponseFailure = httpResponseError.getHttpResponseFailure();
        assertAll(
          () -> assertNotNull(httpResponseFailure),
          () -> assertFalse(httpResponseFailure.isSuccess()),
          () -> assertNull(httpResponseFailure.asSuccess()),
          () -> assertEquals(httpResponseFailure, httpResponseFailure.asFailure()),
          () -> assertEquals(expectedException, httpResponseFailure.getThrowable())
        );
        return true;
      },
      body -> {
        assertNull(body);
        return true;
      }
    );
  }
}
