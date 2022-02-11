package org.example.simple_sample_apis.rest_controllers;

import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.fp.EntityNotFound;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

final class CreateHttpStatus {
  static HttpStatus httpStatusFromEitherError(@NotNull EitherError eitherError) {
    return (eitherError instanceof HasHttpStatusCode) ?
      HttpStatus.valueOf(((HasHttpStatusCode) eitherError).getHttpStatusCode()) :
      (eitherError instanceof EntityNotFound) ?
        HttpStatus.NOT_FOUND :
        HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
