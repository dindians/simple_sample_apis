package org.example.simple_sample_apis.app.usecase_factories;

import reactor.core.publisher.Mono;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.*;
import org.example.simple_sample_apis.http_client.HttpRequest;
import org.example.simple_sample_apis.usecases.AstronomicPicture;
import org.example.simple_sample_apis.usecases.GetAstronomicPictureUsecase;
import static org.example.simple_sample_apis.app.usecase_factories.JsonSerializer.deserialize;

@Component
public final class AstronomicPictureUsecaseFactoryImpl implements AstronomicPictureUsecaseFactory {
  private final ConfigValues configValues;
  private final HttpRequest httpRequest;

  public AstronomicPictureUsecaseFactoryImpl(@NotNull ConfigValues configValues, @NotNull HttpRequest httpRequest) {
    this.configValues = configValues;
    this.httpRequest = httpRequest;
  }

  public GetAstronomicPictureUsecase getAstronomicPictureUsecase() { return new GetAstronomicPictureUsecase(this::getAstronomicPicture); }

  private Mono<Either<EitherError, AstronomicPicture>> getAstronomicPicture(long dummyId) {
    return getJsonFromUri(constructUri(dummyId)).map(eitherErrorOrJson -> eitherErrorOrJson.flatMap(AstronomicPictureUsecaseFactoryImpl::deserializeAstronomicPicture));
  }

  private String constructUri(long dummyId) { return configValues.get("astronomic_picture.api.get"); }

  private Mono<Either<EitherError, String>> getJsonFromUri(@NotNull String uri) { return httpRequest.get(uri).map(HttpResponseMethods::getBody); }

  private static Either<EitherError, AstronomicPicture> deserializeAstronomicPicture(String json) { return deserialize(json, AstronomicPicture.class); }
}
