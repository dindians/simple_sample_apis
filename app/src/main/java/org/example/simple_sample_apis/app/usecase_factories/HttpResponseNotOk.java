package org.example.simple_sample_apis.app.usecase_factories;

import org.jetbrains.annotations.NotNull;
import org.example.simple_sample_apis.fp.EitherError;

final class HttpResponseNotOk implements EitherError, HasHttpStatusCode {
  private final String uri;
  private final Integer httpStatusCode;
  private final String body;

  HttpResponseNotOk(@NotNull String uri, Integer httpStatusCode, String body) {
    this.uri = uri;
    this.httpStatusCode = httpStatusCode;
    this.body = body;
  }

  @Override
  public Integer getHttpStatusCode() { return httpStatusCode; }
  String getBody() { return body; }
  String getUri() { return uri; }
}
