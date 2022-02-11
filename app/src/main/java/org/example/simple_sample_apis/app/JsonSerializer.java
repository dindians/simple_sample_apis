package org.example.simple_sample_apis.app;

import org.jetbrains.annotations.NotNull;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simple_sample_apis.fp.*;

public final class JsonSerializer {
  public static <T> Either<EitherError, T> deserialize(String json, @NotNull Class<T> valueType) { return deserialize(json, valueType, false); }

  public static <T> Either<EitherError, T> deserialize(String json, @NotNull Class<T> valueType, Boolean failOnUnknowProperties) {
    try {
      return Either.right((new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknowProperties).readValue(json, valueType));
    }
    catch(Exception exception) {
      return Either.left(new JsonDeserializationError(json, exception));
    }
  }

  public static Either<EitherError, String> serialize(Object value) {
    try {
      return Either.right(new ObjectMapper().writeValueAsString(value));
    }
    catch(Exception exception) {
      return Either.left(new JsonSerializationError((value != null) ? value.toString() : "value is null", exception));
    }
  }
}
