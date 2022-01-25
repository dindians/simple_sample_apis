package org.example.simple_sample_apis.http_client;

public interface HttpResponseFailure extends HttpResponse {
  Throwable getThrowable();
}
