package org.example.simple_sample_apis.app.usecase_factories;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.usecases.GetPersonWithPictureUsecase;

@Component
public class PersonWithPictureUsecaseFactory {
  private final PersonUsecaseFactory personUsecaseFactory;
  private final AstronomicPictureUsecaseFactory astronomicPictureUsecaseFactory;

  PersonWithPictureUsecaseFactory(@NotNull PersonUsecaseFactory personUsecaseFactory, @NotNull AstronomicPictureUsecaseFactory astronomicPictureUsecaseFactory) {
    this.personUsecaseFactory = personUsecaseFactory;
    this.astronomicPictureUsecaseFactory = astronomicPictureUsecaseFactory;
  }
  public GetPersonWithPictureUsecase getPersonWithPictureUsecase() {
    return new GetPersonWithPictureUsecase(personUsecaseFactory::getPerson, astronomicPictureUsecaseFactory::getAstronomicPicture);
  }
}
