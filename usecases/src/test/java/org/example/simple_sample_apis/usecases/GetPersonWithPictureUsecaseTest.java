package org.example.simple_sample_apis.usecases;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;

public class GetPersonWithPictureUsecaseTest {
  @Test
  void getPersonWithPictureUsecaseConstructor() {
    final Function<Long, Mono<Either<EitherError, Person>>> getPerson = id -> Mono.just(Either.right(new Person(id)));
    final Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture = pictureId -> Mono.just(Either.right(new AstronomicPicture()));

    assertAll(
      () -> assertThrows(IllegalArgumentException.class, () -> new GetPersonWithPictureUsecase(getPerson, null)),
      () -> assertThrows(IllegalArgumentException.class, () -> new GetPersonWithPictureUsecase(null, getAstronomicPicture)),
      () -> assertThrows(IllegalArgumentException.class, () -> new GetPersonWithPictureUsecase(null, null))
    );
  }

  @Test
  void getPersonWithPictureUsecaseSuccess() {
    final Function<Long, Mono<Either<EitherError, Person>>> getPerson = id -> Mono.just(Either.right(new Person(id)));
    final Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture = pictureId -> Mono.just(Either.right(new AstronomicPicture()));
    final var personId = 13L;
    StepVerifier
      .create(new GetPersonWithPictureUsecase(getPerson, getAstronomicPicture).getPersonWithPicture().apply(personId))
      .assertNext(eitherErrorOrPersonWithPicture -> {
        assertAll(
          () -> assertNotNull(eitherErrorOrPersonWithPicture),
          () -> assertTrue(eitherErrorOrPersonWithPicture instanceof Either.Right)
        );
        eitherErrorOrPersonWithPicture.fold(
          eitherError -> {
            assertAll(
              () -> assertNull(eitherError)
            );
            return true;
          },
          personWithPicture -> {
            assertAll(
              () -> assertNotNull(personWithPicture)
            );
            return true;
          });
      })
      .expectComplete()
      .log()
      .verify();
  }

  @Test
  void getPersonWithPictureUsecaseGetPersonFailure() {
    class GetPersonError implements EitherError {}
    final Function<Long, Mono<Either<EitherError, Person>>> getPerson = id -> Mono.just(Either.left(new GetPersonError()));
    final Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture = pictureId -> Mono.just(Either.right(new AstronomicPicture()));
    final var personId = 13L;
    StepVerifier
      .create(new GetPersonWithPictureUsecase(getPerson, getAstronomicPicture).getPersonWithPicture().apply(personId))
      .assertNext(eitherErrorOrPersonWithPicture -> {
        assertAll(
          () -> assertNotNull(eitherErrorOrPersonWithPicture),
          () -> assertTrue(eitherErrorOrPersonWithPicture instanceof Either.Left)
        );
        eitherErrorOrPersonWithPicture.fold(
          eitherError -> {
            assertAll(
              () -> assertNotNull(eitherError),
              () -> assertTrue(eitherError instanceof GetPersonError)
            );
            return true;
          },
          personWithPicture -> {
            assertAll(
              () -> assertNull(personWithPicture)
            );
            return true;
          });
      })
      .expectComplete()
      .log()
      .verify();
  }

  @Test
  void getPersonWithPictureUsecaseGetAstronomicPictureFailure() {
    class GetAstronomicPictureError implements EitherError {}
    final Function<Long, Mono<Either<EitherError, Person>>> getPerson = id -> Mono.just(Either.right(new Person(id)));
    final Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture = pictureId -> Mono.just(Either.left(new GetAstronomicPictureError()));
    final var personId = 13L;
    StepVerifier
      .create(new GetPersonWithPictureUsecase(getPerson, getAstronomicPicture).getPersonWithPicture().apply(personId))
      .assertNext(eitherErrorOrPersonWithPicture -> {
        assertAll(
          () -> assertNotNull(eitherErrorOrPersonWithPicture),
          () -> assertTrue(eitherErrorOrPersonWithPicture instanceof Either.Left)
        );
        eitherErrorOrPersonWithPicture.fold(
          eitherError -> {
            assertAll(
              () -> assertNotNull(eitherError),
              () -> assertTrue(eitherError instanceof GetAstronomicPictureError)
            );
            return true;
          },
          personWithPicture -> {
            assertAll(
              () -> assertNull(personWithPicture)
            );
            return true;
          });
      })
      .expectComplete()
      .log()
      .verify();
  }
}
