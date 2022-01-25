package org.example.simple_sample_apis.usecases;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import java.util.function.Function;

public final class GetAstronomicPictureUsecase {
  private final Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture;

  public GetAstronomicPictureUsecase(@NotNull Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture) {
    this.getAstronomicPicture = getAstronomicPicture;
  }

  public Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture() { return getAstronomicPicture; }
}
