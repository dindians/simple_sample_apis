package org.example.simple_sample_apis.rest_controllers.usecase_factories;

import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.usecases.GetPersonUsecase;
import org.example.simple_sample_apis.usecases.Person;

@Component
public class GetPersonUsecaseFactoryMock implements GetPersonUsecaseFactory {
  @Override
  public GetPersonUsecase getPersonUsecase() { return new GetPersonUsecase(this::getPerson); }

  private Mono<Either<EitherError, Person>> getPerson(Long personId) {
    return Mono.just(Either.right(new Person(personId)));
  }
}
