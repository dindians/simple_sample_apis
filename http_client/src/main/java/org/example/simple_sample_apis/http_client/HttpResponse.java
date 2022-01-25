package org.example.simple_sample_apis.http_client;

public interface HttpResponse {
  String getUri();
  Boolean isSuccess();
  HttpResponseSuccess asSuccess();
  HttpResponseFailure asFailure();
}
