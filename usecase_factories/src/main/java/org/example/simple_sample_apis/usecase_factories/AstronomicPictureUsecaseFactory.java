package org.example.simple_sample_apis.usecase_factories;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.http_client.HttpRequest;
import org.example.simple_sample_apis.usecases.AstronomicPicture;
import org.example.simple_sample_apis.usecases.GetAstronomicPictureUsecase;
import org.example.simple_sample_apis.fp.EitherError;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import static org.example.simple_sample_apis.usecase_factories.Serializer.deserialize;

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

  private static Mono<Either<EitherError, String>> getJsonFromUri(@NotNull String uri) { return HttpRequest.from(uri).get().map(UsecaseFactoryMethods::getBody); }

  private static Either<EitherError, AstronomicPicture> deserializeAstronomicPicture(String json) { return deserialize(json, AstronomicPicture.class); }
}
