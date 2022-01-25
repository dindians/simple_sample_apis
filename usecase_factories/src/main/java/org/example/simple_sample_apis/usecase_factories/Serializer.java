package org.example.simple_sample_apis.usecase_factories;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.jetbrains.annotations.NotNull;

public final class Serializer {
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
      return Either.left(new JsonDeserializationError((value != null) ? value.toString() : "value is null", exception));
    }
  }
}
