package org.example.simple_sample_apis.usecases;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;

public final class GetPersonUsecase {
  private final Function<Long, Mono<Either<EitherError, Person>>> getPerson;

  public GetPersonUsecase(@NotNull Function<Long, Mono<Either<EitherError, Person>>> getPerson) {
    this.getPerson = getPerson;
  }

  public Function<Long, Mono<Either<EitherError, Person>>> getPerson() { return getPerson; }
}
