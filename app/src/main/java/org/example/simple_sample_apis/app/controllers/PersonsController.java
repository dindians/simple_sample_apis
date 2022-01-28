package org.example.simple_sample_apis.app.controllers;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.app.usecase_factories.PersonUsecaseFactory;
import org.example.simple_sample_apis.usecases.GetPersonUsecase;
import static org.example.simple_sample_apis.app.controllers.CreateHttpStatus.httpStatusFromEitherError;

@RestController
@RequestMapping("/persons")
public final class PersonsController {
  private final GetPersonUsecase getPersonUsecase;

  public PersonsController(@NotNull PersonUsecaseFactory personUsecaseFactory) { getPersonUsecase = personUsecaseFactory.getPersonUsecase(); }

  @GetMapping("/{personId}")
  public Mono<String> getPerson(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable long personId) {
    Function<EitherError, String> errorResponse = eitherError -> {
      serverHttpResponse.setStatusCode(httpStatusFromEitherError(eitherError));
      return EitherErrorToJson.execute(eitherError, String.format("rest-controller-class: %s; path: /persons/%d", PersonsController.class.getName(), personId), PersonsController.class);
    };
    Function<String, String> okResponse = json -> {
      serverHttpResponse.setStatusCode(HttpStatus.OK);
      return json;
    };
    return getPersonUsecase.getPerson()
      .apply(personId)
      .map(eitherErrorPersonEither -> ControllerResponses.jsonResponse(eitherErrorPersonEither, errorResponse, okResponse));
  }
}
