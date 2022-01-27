package org.example.simple_sample_apis.app.usecase_factories;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.*;
import org.example.simple_sample_apis.services.PersonFromMemory;
import org.example.simple_sample_apis.services.PersonMemoryService;
import org.example.simple_sample_apis.usecases.GetPersonUsecase;
import org.example.simple_sample_apis.usecases.Person;

@Component
public final class PersonUsecaseFactory {
  public GetPersonUsecase getPersonUsecase() { return new GetPersonUsecase(this::getPerson); }

  Mono<Either<EitherError, Person>> getPerson(long personId) {
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

    return isEven.apply(personFromMemory.getId())?
      Either.right(new Person(personFromMemory.getId())) :
      Either.left(new PersonMappingError(personFromMemory.getId(), String.format("an unexpected error occurred while mapping a person-from-memory to a person. Person-id: %d", personFromMemory.getId())));
  }
}
