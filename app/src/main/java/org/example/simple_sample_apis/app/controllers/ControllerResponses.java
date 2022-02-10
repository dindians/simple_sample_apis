package org.example.simple_sample_apis.app.controllers;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.example.simple_sample_apis.fp.*;
import org.example.simple_sample_apis.app.usecase_factories.JsonSerializer;

final class ControllerResponses {
  static <T> String jsonResponse(@NotNull Either<EitherError, T> eitherErrorOrEntity, @NotNull Function<EitherError, String> failureResponse, @NotNull Function<String, String> successResponse) {
    return eitherErrorOrEntity
      .flatMap(JsonSerializer::serialize)
      .fold(failureResponse, successResponse);
  }
}
