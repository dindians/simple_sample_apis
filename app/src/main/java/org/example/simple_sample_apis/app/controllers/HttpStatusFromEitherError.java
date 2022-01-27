package org.example.simple_sample_apis.app.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.app.usecase_factories.HasHttpStatusCode;

public class HttpStatusFromEitherError {
  static Integer httpStatusCodeFromEitherError(@NotNull EitherError eitherError) {
    return (eitherError instanceof HasHttpStatusCode)? ((HasHttpStatusCode) eitherError).getHttpStatusCode() : HttpStatus.INTERNAL_SERVER_ERROR.value();
  }
  static HttpStatus httpStatusFromEitherError(@NotNull EitherError eitherError) {
    return HttpStatus.valueOf(httpStatusCodeFromEitherError(eitherError));
  }
}
