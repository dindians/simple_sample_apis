package org.example.simple_sample_apis.app.usecase_factories;

import org.example.simple_sample_apis.fp.EitherError;

final class JsonDeserializationError implements EitherError {
  private final String json;
  private final Exception exception;

  JsonDeserializationError(String json, Exception exception) {
    this.json = json;
    this.exception = exception;
  }

  String getJson() { return json; }
  Exception getException() { return exception; }
}