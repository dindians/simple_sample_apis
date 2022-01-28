package org.example.simple_sample_apis.app.controllers;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.example.simple_sample_apis.fp.*;
import org.example.simple_sample_apis.app.usecase_factories.JsonSerializer;

final class ControllerResponses {
  static <T> String jsonResponse(@NotNull Either<EitherError, T> eitherErrorOrEntity, @NotNull Function<EitherError, String> errorResponse, @NotNull Function<String, String> okResponse) {
    return eitherErrorOrEntity
      .flatMap(JsonSerializer::serialize)
      .fold(errorResponse, okResponse);
  }
}
