package org.example.simple_sample_apis.rest_controllers.usecase_factories;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.usecases.GetPersonWithPictureUsecase;

@Component
public final class GetPersonWithPictureUsecaseFactory {
  private final GetPersonUsecaseFactory getPersonUsecaseFactory;
  private final GetAstronomicPictureUsecaseFactory getAstronomicPictureUsecaseFactory;

  public GetPersonWithPictureUsecaseFactory(@NotNull GetPersonUsecaseFactory getPersonUsecaseFactory, @NotNull GetAstronomicPictureUsecaseFactory getAstronomicPictureUsecaseFactory) {
    this.getPersonUsecaseFactory = getPersonUsecaseFactory;
    this.getAstronomicPictureUsecaseFactory = getAstronomicPictureUsecaseFactory;
  }

  public GetPersonWithPictureUsecase getPersonWithPictureUsecase() {
    return new GetPersonWithPictureUsecase(getPersonUsecaseFactory.getPersonUsecase().getPerson(), getAstronomicPictureUsecaseFactory.getAstronomicPictureUsecase().getAstronomicPicture());
  }
}
