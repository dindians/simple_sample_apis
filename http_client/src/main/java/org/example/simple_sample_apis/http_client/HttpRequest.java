package org.example.simple_sample_apis.http_client;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface HttpRequest {
  static HttpRequest from(@NotNull String uri) { return new HttpRequestUsingWebClient(uri); }
  Mono<HttpResponse> get();
  Mono<HttpResponse> delete();
  Mono<HttpResponse> post(String json);
  Mono<HttpResponse> put(String json);
  Mono<HttpResponse> patch(String json);
}
