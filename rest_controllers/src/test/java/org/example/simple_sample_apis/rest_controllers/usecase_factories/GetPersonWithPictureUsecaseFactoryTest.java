package org.example.simple_sample_apis.rest_controllers.usecase_factories;

import reactor.test.StepVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.simple_sample_apis.fp.Either;

public class GetPersonWithPictureUsecaseFactoryTest {
  @Test
  void PersonWithPictureUsecaseFactoryNullArguments() {
    assertAll(
      () -> assertThrows(IllegalArgumentException.class, () -> new GetPersonWithPictureUsecaseFactory(null, new GetAstronomicPictureUsecaseFactoryMock())),
      () -> assertThrows(IllegalArgumentException.class, () -> new GetPersonWithPictureUsecaseFactory(new GetPersonUsecaseFactoryMock(), null)),
      () -> assertThrows(IllegalArgumentException.class, () -> new GetPersonWithPictureUsecaseFactory(null, null))
    );
  }

  @Test
  void PersonWithPictureUsecaseFactory() {
    final var personWithPictureUsecaseFactory = new GetPersonWithPictureUsecaseFactory(new GetPersonUsecaseFactoryMock(), new GetAstronomicPictureUsecaseFactoryMock());
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
