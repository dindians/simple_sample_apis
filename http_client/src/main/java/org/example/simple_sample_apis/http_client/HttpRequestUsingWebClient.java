package org.example.simple_sample_apis.http_client;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public final class HttpRequestUsingWebClient implements HttpRequest {
  @Override
  public Mono<HttpResponse> get(@NotNull String uri) {
    return WebClient
      .create()
      .get()
      .uri(uri)
      .accept(MediaType.APPLICATION_JSON)
      .exchangeToMono(clientResponse ->
        clientResponse
          .bodyToMono(String.class)
          .map(body -> WebClientResponse.responseSuccess(clientResponse.rawStatusCode(), body, uri)))
      .onErrorResume(throwable ->
        Mono.just(WebClientError.responseFailure(throwable, uri)));
  }

  @Override
  public Mono<HttpResponse> delete(@NotNull String uri) { throw new HttpRequestMethodNotImplementedException(); }
  @Override
  public Mono<HttpResponse> post(@NotNull String uri, String json) { throw new HttpRequestMethodNotImplementedException(); }
  @Override
  public Mono<HttpResponse> put(@NotNull String uri, String json) { throw new HttpRequestMethodNotImplementedException(); }
  @Override
  public Mono<HttpResponse> patch(@NotNull String uri, String json) { throw new HttpRequestMethodNotImplementedException(); }

  private final static class HttpRequestMethodNotImplementedException extends RuntimeException {}

  private final static class WebClientResponse implements HttpResponseSuccess {
    private final Integer statusCode;
    private final String body;
    private final String uri;

    private WebClientResponse(@NotNull Integer statusCode, @NotNull String body, @NotNull String uri) {
      this.statusCode = statusCode;
      this.body = body;
      this.uri = uri;
    }

    static HttpResponse responseSuccess(@NotNull Integer statusCode, @NotNull String body, @NotNull String uri) { return new WebClientResponse(statusCode, body, uri); }

    @Override
    public Boolean isSuccess() { return true; }
    @Override
    public HttpResponseSuccess asSuccess() {return this; }
    @Override
    public HttpResponseFailure asFailure() {return null; }
    @Override
    public String getUri() { return uri; }
    @Override
    public Integer getStatusCode() { return statusCode; }
    @Override
    public String getBody() { return body; }
  }

  private final static class WebClientError implements HttpResponseFailure {
    private final Throwable throwable;
    private final String uri;

    private WebClientError(@NotNull Throwable throwable, @NotNull String uri) {
      this.throwable = throwable;
      this.uri = uri;
    }

    static HttpResponse responseFailure(@NotNull Throwable throwable, @NotNull String uri) { return new WebClientError((throwable), uri); }

    @Override
    public String getUri() { return uri; }
    @Override
    public Boolean isSuccess() { return false; }
    @Override
    public HttpResponseSuccess asSuccess() {return null; }
    @Override
    public HttpResponseFailure asFailure() {return this; }
    @Override
    public Throwable getThrowable() { return throwable; }
  }
}
