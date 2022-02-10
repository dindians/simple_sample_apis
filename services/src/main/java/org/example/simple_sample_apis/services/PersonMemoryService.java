package org.example.simple_sample_apis.services;

import java.util.function.Function;
import reactor.core.publisher.Mono;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.fp.EntityNotFound;

public final class PersonMemoryService {
  public Mono<Either<EitherError, PersonFromMemory>>  getPerson(long personId) {
    final class PersonMemoryServiceError implements EitherError {
      private final Long personId;
      private final String errorText;

      PersonMemoryServiceError(Long personId, String errorText) {
        this.personId = personId;
        this.errorText = errorText;
      }

      public Long getPersonId() { return personId; }
      public String getErrorText() { return errorText; }
    }

    final class PersonFromMemoryNotFound implements EntityNotFound {
      private final Long personId;

      PersonFromMemoryNotFound(Long personId) {
        this.personId = personId;
      }
      public Long getPersonId() { return personId; }
    }

    final Function<Long, Boolean> isEven = x -> (x & 1) == 0;
    final Function<Long, Boolean> isTenFold = x -> (x % 10) == 0;

    return Mono.just(
      isEven.apply(personId)?
        isTenFold.apply(personId)?
          Either.left(new PersonFromMemoryNotFound(personId)) :
          Either.right(new PersonFromMemory(personId)) :
        Either.left(new PersonMemoryServiceError(personId, String.format("getting a person-from-memory for an odd person-id results in an error. Person-id: %d", personId))));
  }
}
