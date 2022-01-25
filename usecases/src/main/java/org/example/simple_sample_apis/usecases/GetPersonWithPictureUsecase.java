package org.example.simple_sample_apis.usecases;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public final class GetPersonWithPictureUsecase {
  private final Function<Long, Mono<Either<EitherError, Person>>> getPerson;
  private final Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture;

  public GetPersonWithPictureUsecase(@NotNull Function<Long, Mono<Either<EitherError, Person>>> getPerson, @NotNull Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture) {
    this.getPerson = getPerson;
    this.getAstronomicPicture = getAstronomicPicture;
  }

  public Function<Long, Mono<Either<EitherError, PersonWithPicture>>> etPersonWithPicture() { return this::getPersonWithPicture; }

  private Mono<Either<EitherError, PersonWithPicture>> getPersonWithPicture(long personId) {
    return getPerson.apply(personId)
      .flatMap(eitherErrorOrPerson -> eitherErrorOrPerson.flatMapMono(person -> getAstronomicPicture.apply(person.getId())))
      .map(eitherErrorOrAstronomicPicture -> eitherErrorOrAstronomicPicture.map(astronomicPicture -> new PersonWithPicture(personId, astronomicPicture.getHdurl(), astronomicPicture.getTitle(), astronomicPicture.getExplanation())));
  }
}
