package org.example.simple_sample_apis.usecase_factories;

import org.example.simple_sample_apis.fp.EitherError;

public final class JsonDeserializationError implements EitherError {
  private final String json;
  private final Exception exception;

  JsonDeserializationError(String json, Exception exception) {
    this.json = json;
    this.exception = exception;
  }

  public String getJson() { return json; }
  public Exception getException() { return exception; }
}
