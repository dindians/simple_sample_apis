package org.example.simple_sample_apis.app.usecase_factories;

import reactor.core.publisher.Mono;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.*;
import org.example.simple_sample_apis.http_client.HttpRequest;
import org.example.simple_sample_apis.usecases.AstronomicPicture;
import org.example.simple_sample_apis.usecases.GetAstronomicPictureUsecase;
import static org.example.simple_sample_apis.app.usecase_factories.Serializer.deserialize;

@Component
public final class AstronomicPictureUsecaseFactory {
  private final ConfigValues configValues;

  public AstronomicPictureUsecaseFactory(@NotNull ConfigValues configValues) {
    this.configValues = configValues;
  }

  public GetAstronomicPictureUsecase getAstronomicPictureUsecase() { return new GetAstronomicPictureUsecase(this::getAstronomicPicture); }

  Mono<Either<EitherError, AstronomicPicture>> getAstronomicPicture(long dummyId) {
    return getJsonFromUri(constructUri(dummyId)).map(eitherErrorOrJson -> eitherErrorOrJson.flatMap(AstronomicPictureUsecaseFactory::deserializeAstronomicPicture));
  }

  private String constructUri(long dummyId) { return configValues.get("astronomic_picture.api.get"); }

  private static Mono<Either<EitherError, String>> getJsonFromUri(@NotNull String uri) { return HttpRequest.from(uri).get().map(HttpResponseMethods::getBody); }

  private static Either<EitherError, AstronomicPicture> deserializeAstronomicPicture(String json) { return deserialize(json, AstronomicPicture.class); }
}
