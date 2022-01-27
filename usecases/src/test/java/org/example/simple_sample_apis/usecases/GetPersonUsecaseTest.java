package org.example.simple_sample_apis.usecases;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetPersonUsecaseTest {
  @Test
  @Disabled
  void getPersonUsecaseConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new GetPersonUsecase(null));
  }

  @Test
  void getPersonUsecaseSuccess() {
    final Function<Long, Mono<Either<EitherError, Person>>> getPerson = id -> Mono.just(Either.right(new Person(id)));
    final var personId = 13L;
    StepVerifier
      .create(new GetPersonUsecase(getPerson).getPerson().apply(personId))
      .assertNext(eitherErrorOrPerson -> {
        assertAll(
          () -> assertNotNull(eitherErrorOrPerson),
          () -> assertTrue(eitherErrorOrPerson instanceof Either.Right)
        );
        eitherErrorOrPerson.fold(
          eitherError -> {
            assertAll(
              () -> assertNull(eitherError)
            );
            return true;
          },
          person -> {
            assertAll(
              () -> assertNotNull(person)
            );
            return true;
          }
        );
      })
      .expectComplete()
      .log()
      .verify();
  }

  @Test
  void getPersonUsecaseFailure() {
    class GetPersonError implements EitherError {}
    final Function<Long, Mono<Either<EitherError, Person>>> getPerson = id -> Mono.just(Either.left(new GetPersonError()));
    final var personId = 13L;
    StepVerifier
      .create(new GetPersonUsecase(getPerson).getPerson().apply(personId))
      .assertNext(eitherErrorOrPerson -> {
        assertAll(
          () -> assertNotNull(eitherErrorOrPerson),
          () -> assertTrue(eitherErrorOrPerson instanceof Either.Left)
        );
        eitherErrorOrPerson.fold(
          eitherError -> {
            assertAll(
              () -> assertNotNull(eitherError),
              () -> assertTrue(eitherError instanceof GetPersonError)
            );
            return true;
          },
          person -> {
            assertAll(
              () -> assertNull(person)
            );
            return true;
          }
        );
      })
      .expectComplete()
      .log()
      .verify();
  }
}
