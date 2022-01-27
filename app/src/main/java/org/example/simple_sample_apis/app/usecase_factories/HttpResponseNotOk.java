package org.example.simple_sample_apis.app.usecase_factories;

import org.jetbrains.annotations.NotNull;
import org.example.simple_sample_apis.fp.EitherError;

public final class HttpResponseNotOk implements EitherError, HasHttpStatusCode {
  private final String uri;
  private final Integer httpStatusCode;
  private final String body;

  public HttpResponseNotOk(@NotNull String uri, Integer httpStatusCode, String body) {
    this.uri = uri;
    this.httpStatusCode = httpStatusCode;
    this.body = body;
  }

  @Override
  public Integer getHttpStatusCode() { return httpStatusCode; }
  public String getBody() { return body; }
  public String getUri() { return uri; }
}
