package org.example.simple_sample_apis.services;

import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import reactor.core.publisher.Mono;

public final class PersonMemoryService {
  public Mono<Either<EitherError, PersonFromMemory>>  getPerson(long personId) {
    return Mono.just(Either.right(new PersonFromMemory(personId)));
  }
}
