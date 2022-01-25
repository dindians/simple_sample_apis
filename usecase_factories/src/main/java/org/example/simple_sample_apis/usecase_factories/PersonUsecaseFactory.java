package org.example.simple_sample_apis.usecase_factories;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.services.PersonFromMemory;
import org.example.simple_sample_apis.services.PersonMemoryService;
import org.example.simple_sample_apis.usecases.GetPersonUsecase;
import org.example.simple_sample_apis.usecases.Person;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public final class PersonUsecaseFactory {
  public GetPersonUsecase getPersonUsecase() { return new GetPersonUsecase(this::getPerson); }

  Mono<Either<EitherError, Person>> getPerson(long personId) {
    return getPersonFromMemory(personId).map(eitherErrorOrPersonFromMemory -> eitherErrorOrPersonFromMemory.map(PersonUsecaseFactory::personFromMemoryToPerson));
  }

  private Mono<Either<EitherError, PersonFromMemory>> getPersonFromMemory(long personId) { return new PersonMemoryService().getPerson(personId); }

  private static Person personFromMemoryToPerson(@NotNull PersonFromMemory personFromMemory) { return new Person(personFromMemory.getId()); }
}
