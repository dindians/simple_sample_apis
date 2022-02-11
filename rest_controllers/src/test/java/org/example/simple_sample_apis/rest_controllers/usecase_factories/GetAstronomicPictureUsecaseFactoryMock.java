package org.example.simple_sample_apis.rest_controllers.usecase_factories;

import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.usecases.AstronomicPicture;
import org.example.simple_sample_apis.usecases.GetAstronomicPictureUsecase;

@Component
public class GetAstronomicPictureUsecaseFactoryMock implements GetAstronomicPictureUsecaseFactory {
  @Override
  public GetAstronomicPictureUsecase getAstronomicPictureUsecase() {
    return new GetAstronomicPictureUsecase(this::getAstronomicPicture);
  }

  private Mono<Either<EitherError, AstronomicPicture>> getAstronomicPicture(Long astronomicPictureId) {
    return Mono.just(Either.right(new AstronomicPicture()));
  }
}
