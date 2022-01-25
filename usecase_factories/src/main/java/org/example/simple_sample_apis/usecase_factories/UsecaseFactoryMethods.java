package org.example.simple_sample_apis.usecase_factories;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.http_client.HttpResponse;
import org.example.simple_sample_apis.http_client.HttpResponseSuccess;
import org.example.simple_sample_apis.fp.EitherError;
import org.jetbrains.annotations.NotNull;

public final class UsecaseFactoryMethods {
  public static Either<EitherError,String> getBody(@NotNull HttpResponse httpResponse) {
    if(!httpResponse.isSuccess()) return Either.left(new HttpResponseError(httpResponse.getUri(), httpResponse.asFailure()));
    final HttpResponseSuccess httpResponseSuccess = httpResponse.asSuccess();
    return httpResponseSuccess.isOk() ? Either.right(httpResponseSuccess.getBody()) : Either.left(new HttpResponseNotOk(httpResponseSuccess.getUri(), httpResponseSuccess.getStatusCode(), httpResponseSuccess.getBody()));
  }
}
