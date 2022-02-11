package org.example.simple_sample_apis.rest_controllers.controllers;

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
import org.example.simple_sample_apis.usecases.GetPersonWithPictureUsecase;
import org.example.simple_sample_apis.rest_controllers.usecase_factories.GetPersonWithPictureUsecaseFactory;
import org.example.simple_sample_apis.rest_controllers.ControllerResponses;
import org.example.simple_sample_apis.rest_controllers.CreateHttpStatus;
import org.example.simple_sample_apis.rest_controllers.EitherErrorToJson;

@RestController
@RequestMapping(value = "/persons-with-picture", produces = MediaType.APPLICATION_JSON_VALUE)
public final class GetPersonsWithPictureController {
  private final GetPersonWithPictureUsecase getPersonWithPictureUsecase;

  public GetPersonsWithPictureController(@NotNull GetPersonWithPictureUsecaseFactory getPersonWithPictureUsecaseFactory) {
    getPersonWithPictureUsecase = getPersonWithPictureUsecaseFactory.getPersonWithPictureUsecase();
  }

  @GetMapping("/{personId}")
  public Mono<String> getPersonWithPicture(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable long personId) {
    Function<EitherError, String> failureResponse = eitherError -> {
      serverHttpResponse.setStatusCode(CreateHttpStatus.httpStatusFromEitherError(eitherError));
      return EitherErrorToJson.execute(eitherError, String.format("rest-controller-class: %s; method: GET; path: /persons-with-picture/%d", GetPersonsWithPictureController.class.getName(), personId), GetPersonsWithPictureController.class);
    };
    Function<String, String> successResponse = json -> {
      serverHttpResponse.setStatusCode(HttpStatus.OK);
      return json;
    };
    return getPersonWithPictureUsecase.getPersonWithPicture()
      .apply(personId)
      .map(eitherErrorOrPersonWithPicture -> ControllerResponses.jsonResponse(eitherErrorOrPersonWithPicture, failureResponse, successResponse));
  }
}
