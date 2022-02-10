package org.example.simple_sample_apis.app.usecase_factories;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.usecases.*;

public class PersonWithPictureUsecaseFactoryTest {
  static class GetPersonUsecaseFactoryDummyImpl implements GetPersonUsecaseFactory {

    @Override
    public GetPersonUsecase getPersonUsecase() {
      return new GetPersonUsecase(personId -> Mono.just(Either.right(new Person(personId))));
    }
  }

  static class GetAstronomicPictureUsecaseFactoryDummyImpl implements GetAstronomicPictureUsecaseFactory {

    @Override
    public GetAstronomicPictureUsecase getAstronomicPictureUsecase() {
      return new GetAstronomicPictureUsecase(astronomicPictureId -> Mono.just(Either.right(new AstronomicPicture())));
    }
  }

  @Test
  void PersonWithPictureUsecaseFactoryNullArguments() {
    assertAll(
      () -> assertThrows(IllegalArgumentException.class, () -> new GetPersonWithPictureUsecaseFactory(null, new GetAstronomicPictureUsecaseFactoryDummyImpl())),
      () -> assertThrows(IllegalArgumentException.class, () -> new GetPersonWithPictureUsecaseFactory(new GetPersonUsecaseFactoryDummyImpl(), null)),
      () -> assertThrows(IllegalArgumentException.class, () -> new GetPersonWithPictureUsecaseFactory(null, null))
    );
  }

  @Test
  void PersonWithPictureUsecaseFactory() {
    final var personWithPictureUsecaseFactory = new GetPersonWithPictureUsecaseFactory(new GetPersonUsecaseFactoryDummyImpl(), new GetAstronomicPictureUsecaseFactoryDummyImpl());
    assertAll(
      () -> assertNotNull(personWithPictureUsecaseFactory),
      () -> assertNotNull(personWithPictureUsecaseFactory.getPersonWithPictureUsecase()),
      () -> assertNotNull(personWithPictureUsecaseFactory.getPersonWithPictureUsecase().getPersonWithPicture())
    );
    final var personId = 13L;
    StepVerifier
      .create(personWithPictureUsecaseFactory.getPersonWithPictureUsecase().getPersonWithPicture().apply(personId))
      .assertNext(eitherErrorOrPersonWithPicture -> {
        assertAll(
          () -> assertNotNull(eitherErrorOrPersonWithPicture),
          () -> assertTrue(eitherErrorOrPersonWithPicture instanceof Either.Right)
        );
        eitherErrorOrPersonWithPicture.fold(
          eitherError -> {
            assertNull(eitherError);
            return true;
          },
          personWithPicture -> {
            assertAll(
              () -> assertNotNull(personWithPicture),
              () -> assertEquals(personId, personWithPicture.getId())
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
