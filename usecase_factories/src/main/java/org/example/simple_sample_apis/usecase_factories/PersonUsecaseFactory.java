package org.example.simple_sample_apis.usecase_factories;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.services.PersonFromMemory;
import org.example.simple_sample_apis.services.PersonMemoryService;
import org.example.simple_sample_apis.usecases.GetPersonUsecase;
import org.example.simple_sample_apis.usecases.Person;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.function.Function;

@Component
public final class PersonUsecaseFactory {
  public GetPersonUsecase getPersonUsecase() { return new GetPersonUsecase(this::getPerson); }

  private Mono<Either<EitherError, Person>> getPerson(long personId) {
    return getPersonFromMemory(personId).map(eitherErrorOrPersonFromMemory -> eitherErrorOrPersonFromMemory.flatMap(PersonUsecaseFactory::personFromMemoryToPerson));
  }

  private Mono<Either<EitherError, PersonFromMemory>> getPersonFromMemory(long personId) { return new PersonMemoryService().getPerson(personId); }

  private static Either<EitherError,Person> personFromMemoryToPerson(@NotNull PersonFromMemory personFromMemory) {
    class PersonMappingError implements EitherError {
      private final Long personId;
      private final String errorText;

      PersonMappingError(Long personId, String errorText) {
        this.personId = personId;
        this.errorText = errorText;
      }

      public Long getPersonId() { return personId; }
      public String getErrorText() { return errorText; }
    }

    final Function<Long, Boolean> isEven = x -> (x & 1) == 0;

    if(isEven.apply(personFromMemory.getId())) { return Either.right(new Person(personFromMemory.getId())); }
    return Either.left(new PersonMappingError(personFromMemory.getId(), String.format("an unexpected error occurred while mapping a person-from-memory to a person. Person-id: %d", personFromMemory.getId())));
  }
}
