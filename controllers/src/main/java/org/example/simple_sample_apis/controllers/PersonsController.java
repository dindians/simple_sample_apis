package org.example.simple_sample_apis.controllers;

import org.example.simple_sample_apis.usecase_factories.PersonUsecaseFactory;
import org.example.simple_sample_apis.usecase_factories.Serializer;
import org.example.simple_sample_apis.usecases.GetPersonUsecase;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import static org.example.simple_sample_apis.controllers.HttpStatusFromEitherError.httpStatusFromEitherError;

@RestController
@RequestMapping("/persons")
public class PersonsController {
  private final GetPersonUsecase getPersonUsecase;

  public PersonsController(@NotNull PersonUsecaseFactory personUsecaseFactory) { getPersonUsecase = personUsecaseFactory.getPersonUsecase(); }

  @GetMapping("/{personId}")
  public Mono<String> getPerson(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable long personId) {
    return getPersonUsecase.getPerson().apply(personId)
      .map(eitherErrorPersonEither ->
        eitherErrorPersonEither
          .flatMap(Serializer::serialize)
          .fold(
            eitherError -> {
              serverHttpResponse.setStatusCode(httpStatusFromEitherError(eitherError));
              return ErrorHandling.error(eitherError, String.format("rest-controller-class: %s; path: /persons/%d", PersonsController.class.getName(), personId), PersonsController.class);
            },
            personJson -> {
              serverHttpResponse.setStatusCode(HttpStatus.OK);
              return personJson;
            }
          )
      );
  }
}
