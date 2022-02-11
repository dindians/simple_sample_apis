package org.example.simple_sample_apis.rest_controllers;

import org.jetbrains.annotations.NotNull;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;

public final class JsonSerializer {
  public static final class JsonSerializationError implements EitherError {
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
  public static final class JsonDeserializationError implements EitherError {
    private final String json;
    private final Exception exception;

    public JsonDeserializationError(String json, Exception exception) {
      this.json = json;
      this.exception = exception;
    }

    public String getJson() { return json; }
    public Exception getException() { return exception; }
  }

  public static <T> Either<EitherError, T> deserialize(String json, @NotNull Class<T> valueType) { return deserialize(json, valueType, false); }

  public static <T> Either<EitherError, T> deserialize(String json, @NotNull Class<T> valueType, Boolean failOnUnknowProperties) {
    try {
      return Either.right((new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknowProperties).readValue(json, valueType));
    }
    catch(Exception exception) {
      return Either.left(new JsonDeserializationError(json, exception));
    }
  }

  //todo only the function is used in the rest_controllers module. Push this class back to the app module.
  public static Either<EitherError, String> serialize(Object value) {
    try {
      return Either.right(new ObjectMapper().writeValueAsString(value));
    }
    catch(Exception exception) {
      return Either.left(new JsonSerializationError((value != null) ? value.toString() : "value is null", exception));
    }
  }
}

