package org.example.simple_sample_apis.app.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.app.usecase_factories.HasHttpStatusCode;

final class CreateHttpStatus {
  static HttpStatus httpStatusFromEitherError(@NotNull EitherError eitherError) {
    return (eitherError instanceof HasHttpStatusCode)? HttpStatus.valueOf(((HasHttpStatusCode) eitherError).getHttpStatusCode()) : HttpStatus.INTERNAL_SERVER_ERROR;
  }
}