package org.example.simple_sample_apis.app.controllers;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.app.usecase_factories.GetPersonUsecaseFactory;
import org.example.simple_sample_apis.usecases.GetPersonUsecase;
import static org.example.simple_sample_apis.app.controllers.CreateHttpStatus.httpStatusFromEitherError;

@RestController
@RequestMapping(value = "/persons", produces = MediaType.APPLICATION_JSON_VALUE)
public final class GetPersonsController {
  private final GetPersonUsecase getPersonUsecase;

  public GetPersonsController(@NotNull GetPersonUsecaseFactory getPersonUsecaseFactory) { getPersonUsecase = getPersonUsecaseFactory.getPersonUsecase(); }

  @GetMapping("/{personId}")
  public Mono<String> getPerson(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable long personId) {
    Function<EitherError, String> failureResponse = eitherError -> {
      serverHttpResponse.setStatusCode(httpStatusFromEitherError(eitherError));
      return EitherErrorToJson.execute(eitherError, String.format("rest-controller-class: %s; method: GET; path: /persons/%d", GetPersonsController.class.getName(), personId), GetPersonsController.class);
    };
    Function<String, String> successResponse = json -> {
      serverHttpResponse.setStatusCode(HttpStatus.OK);
      return json;
    };
    return getPersonUsecase.getPerson()
      .apply(personId)
      .map(eitherErrorOrPerson -> ControllerResponses.jsonResponse(eitherErrorOrPerson, failureResponse, successResponse));
  }
}
