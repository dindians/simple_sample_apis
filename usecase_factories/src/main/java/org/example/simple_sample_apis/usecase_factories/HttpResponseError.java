package org.example.simple_sample_apis.usecase_factories;

import org.example.simple_sample_apis.http_client.HttpResponseFailure;
import org.example.simple_sample_apis.fp.EitherError;
import org.jetbrains.annotations.NotNull;

public final class HttpResponseError implements EitherError {
  private final String uri;
  private final HttpResponseFailure httpResponseFailure;

  HttpResponseError(String uri, @NotNull HttpResponseFailure httpResponseFailure) {
    this.uri = uri;
    this.httpResponseFailure = httpResponseFailure;
  }

  public String getUri() { return uri; }
  public HttpResponseFailure getHttpResponseFailure() { return httpResponseFailure; }
}
