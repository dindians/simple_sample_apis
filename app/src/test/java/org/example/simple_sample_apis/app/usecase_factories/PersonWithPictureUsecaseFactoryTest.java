package org.example.simple_sample_apis.app.usecase_factories;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.usecases.*;

public class PersonWithPictureUsecaseFactoryTest {
  static class PersonUsecaseFactoryDummyImpl implements PersonUsecaseFactory {

    @Override
    public GetPersonUsecase getPersonUsecase() {
      return new GetPersonUsecase(personId -> Mono.just(Either.right(new Person(personId))));
    }
  }

  static class AstronomicPictureUsecaseFactoryDummyImpl implements AstronomicPictureUsecaseFactory {

    @Override
    public GetAstronomicPictureUsecase getAstronomicPictureUsecase() {
      return new GetAstronomicPictureUsecase(astronomicPictureId -> Mono.just(Either.right(new AstronomicPicture())));
    }
  }

  @Test
  void PersonWithPictureUsecaseFactoryNullArguments() {
    assertAll(
      () -> assertThrows(IllegalArgumentException.class, () -> new PersonWithPictureUsecaseFactory(null, new AstronomicPictureUsecaseFactoryDummyImpl())),
      () -> assertThrows(IllegalArgumentException.class, () -> new PersonWithPictureUsecaseFactory(new PersonUsecaseFactoryDummyImpl(), null)),
      () -> assertThrows(IllegalArgumentException.class, () -> new PersonWithPictureUsecaseFactory(null, null))
    );
  }

  @Test
  void PersonWithPictureUsecaseFactory() {
    final var personWithPictureUsecaseFactory = new PersonWithPictureUsecaseFactory(new PersonUsecaseFactoryDummyImpl(), new AstronomicPictureUsecaseFactoryDummyImpl());
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
