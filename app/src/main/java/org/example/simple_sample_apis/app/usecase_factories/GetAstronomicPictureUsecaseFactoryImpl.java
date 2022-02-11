package org.example.simple_sample_apis.app.usecase_factories;

import reactor.core.publisher.Mono;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.*;
import org.example.simple_sample_apis.http_client.HttpRequest;
import org.example.simple_sample_apis.usecases.AstronomicPicture;
import org.example.simple_sample_apis.usecases.GetAstronomicPictureUsecase;
import static org.example.simple_sample_apis.app.JsonSerializer.deserialize;
import org.example.simple_sample_apis.rest_controllers.usecase_factories.GetAstronomicPictureUsecaseFactory;
import org.example.simple_sample_apis.app.ConfigValues;
import org.example.simple_sample_apis.app.HttpResponseMethods;

@Component
public final class GetAstronomicPictureUsecaseFactoryImpl implements GetAstronomicPictureUsecaseFactory {
  private final ConfigValues configValues;
  private final HttpRequest httpRequest;

  public GetAstronomicPictureUsecaseFactoryImpl(@NotNull ConfigValues configValues, @NotNull HttpRequest httpRequest) {
    this.configValues = configValues;
    this.httpRequest = httpRequest;
  }

  @Override
  public GetAstronomicPictureUsecase getAstronomicPictureUsecase() { return new GetAstronomicPictureUsecase(this::getAstronomicPicture); }

  private Mono<Either<EitherError, AstronomicPicture>> getAstronomicPicture(long dummyId) {
    return getJsonFromUri(constructUri(dummyId)).map(eitherErrorOrJson -> eitherErrorOrJson.flatMap(GetAstronomicPictureUsecaseFactoryImpl::deserializeAstronomicPicture));
  }

  private String constructUri(long dummyId) { return configValues.get("astronomic_picture.api.get"); }

  private Mono<Either<EitherError, String>> getJsonFromUri(@NotNull String uri) { return httpRequest.get(uri).map(HttpResponseMethods::getBody); }

  private static Either<EitherError, AstronomicPicture> deserializeAstronomicPicture(String json) { return deserialize(json, AstronomicPicture.class); }
}
