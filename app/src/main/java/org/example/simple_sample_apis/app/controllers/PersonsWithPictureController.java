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
import org.example.simple_sample_apis.app.usecase_factories.PersonWithPictureUsecaseFactory;
import org.example.simple_sample_apis.usecases.GetPersonWithPictureUsecase;
import static org.example.simple_sample_apis.app.controllers.CreateHttpStatus.httpStatusFromEitherError;

@RestController
@RequestMapping(value = "/persons-with-picture", produces = MediaType.APPLICATION_JSON_VALUE)
public final class PersonsWithPictureController {
  private final GetPersonWithPictureUsecase getPersonWithPictureUsecase;

  public PersonsWithPictureController(@NotNull PersonWithPictureUsecaseFactory personWithPictureUsecaseFactory) {
    getPersonWithPictureUsecase = personWithPictureUsecaseFactory.getPersonWithPictureUsecase();
  }

  @GetMapping("/{personId}")
  public Mono<String> getPersonWithPicture(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable long personId) {
    Function<EitherError, String> errorResponse = eitherError -> {
      serverHttpResponse.setStatusCode(httpStatusFromEitherError(eitherError));
      return EitherErrorToJson.execute(eitherError, String.format("rest-controller-class: %s; path: /persons-with-picture/%d", PersonsWithPictureController.class.getName(), personId), PersonsWithPictureController.class);
    };
    Function<String, String> okResponse = json -> {
      serverHttpResponse.setStatusCode(HttpStatus.OK);
      return json;
    };
    return getPersonWithPictureUsecase.getPersonWithPicture()
      .apply(personId)
      .map(eitherErrorOrPersonWithPicture -> ControllerResponses.jsonResponse(eitherErrorOrPersonWithPicture, errorResponse, okResponse));
  }
}
