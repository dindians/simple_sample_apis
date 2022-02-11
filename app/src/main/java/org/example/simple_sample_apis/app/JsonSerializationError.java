package org.example.simple_sample_apis.app;

import org.jetbrains.annotations.NotNull;
import org.example.simple_sample_apis.fp.EitherError;

public final class JsonSerializationError implements EitherError {
  private final String json;
  private final String exceptionType;
  private final Exception exception;

  public JsonSerializationError(@NotNull String json, @NotNull Exception exception) {
    this.json = json;
    this.exceptionType = exception.getClass().getName();
    this.exception = exception;
  }

  public String getJson() { return json; }
  public String getExceptionType() { return exceptionType; }
  public Exception getException() { return exception; }
}
