package org.example.simple_sample_apis.http_client;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface HttpRequest {
  Mono<HttpResponse> get(@NotNull String uri);
  Mono<HttpResponse> delete(@NotNull String uri);
  Mono<HttpResponse> post(@NotNull String uri, String json);
  Mono<HttpResponse> put(@NotNull String uri, String json);
  Mono<HttpResponse> patch(@NotNull String uri, String json);
}
