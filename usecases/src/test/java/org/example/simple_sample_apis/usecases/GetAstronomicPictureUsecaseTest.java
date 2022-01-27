package org.example.simple_sample_apis.usecases;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;

public class GetAstronomicPictureUsecaseTest {
  @Test
  @Disabled
  void getAstronomicPictureUsecaseConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new GetAstronomicPictureUsecase(null));
  }

  @Test
  void getAstronomicPictureUsecaseSuccess() {
    final Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture = pictureId -> Mono.just(Either.right(new AstronomicPicture()));
    final var dummyId = 13L;
    StepVerifier
      .create(new GetAstronomicPictureUsecase(getAstronomicPicture).getAstronomicPicture().apply(dummyId))
      .assertNext(eitherErrorOrAstronomicPicture -> {
        assertAll(
          () -> assertNotNull(eitherErrorOrAstronomicPicture),
          () -> assertTrue(eitherErrorOrAstronomicPicture instanceof Either.Right)
        );
        eitherErrorOrAstronomicPicture.fold(
          eitherError -> {
            assertAll(
              () -> assertNull(eitherError)
            );
            return true;
          },
          astronomicPicture -> {
            assertAll(
              () -> assertNotNull(astronomicPicture)
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
  void getAstronomicPictureUsecaseFailure() {
    class GetAstronomicPictureError implements EitherError {}
    final Function<Long, Mono<Either<EitherError, AstronomicPicture>>> getAstronomicPicture = pictureId -> Mono.just(Either.left(new GetAstronomicPictureError()));
    final var dummyId = 13L;
    StepVerifier
      .create(new GetAstronomicPictureUsecase(getAstronomicPicture).getAstronomicPicture().apply(dummyId))
      .assertNext(eitherErrorOrAstronomicPicture -> {
        assertAll(
          () -> assertNotNull(eitherErrorOrAstronomicPicture),
          () -> assertTrue(eitherErrorOrAstronomicPicture instanceof Either.Left)
        );
        eitherErrorOrAstronomicPicture.fold(
          eitherError -> {
            assertAll(
              () -> assertNotNull(eitherError),
              () -> assertTrue(eitherError instanceof GetAstronomicPictureError)
            );
            return true;
          },
          astronomicPicture -> {
            assertAll(
              () -> assertNull(astronomicPicture)
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
