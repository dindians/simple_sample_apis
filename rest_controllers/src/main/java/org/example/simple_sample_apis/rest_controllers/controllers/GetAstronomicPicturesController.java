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
import org.example.simple_sample_apis.usecases.GetAstronomicPictureUsecase;
import org.example.simple_sample_apis.rest_controllers.usecase_factories.GetAstronomicPictureUsecaseFactory;
import org.example.simple_sample_apis.rest_controllers.ControllerResponses;
import org.example.simple_sample_apis.rest_controllers.CreateHttpStatus;
import org.example.simple_sample_apis.rest_controllers.EitherErrorToJson;

@RestController
@RequestMapping(value = "/astronomic-pictures", produces = MediaType.APPLICATION_JSON_VALUE)
public final class GetAstronomicPicturesController {
  private final GetAstronomicPictureUsecase getAstronomicPictureUsecase;

  public GetAstronomicPicturesController(@NotNull GetAstronomicPictureUsecaseFactory getAstronomicPictureUsecaseFactory) {
    getAstronomicPictureUsecase = getAstronomicPictureUsecaseFactory.getAstronomicPictureUsecase();
  }

  @GetMapping("/{id}")
  public Mono<String> getAstronomicPicture(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable long id) {
    Function<EitherError, String> failureResponse = eitherError -> {
      serverHttpResponse.setStatusCode(CreateHttpStatus.httpStatusFromEitherError(eitherError));
      return EitherErrorToJson.execute(eitherError,String.format("rest-controller-class: %s; method: GET; path: /astronomic-pictures/%d", GetAstronomicPicturesController.class.getName(), id), GetAstronomicPicturesController.class);
    };
    Function<String, String> successResponse = json -> {
      serverHttpResponse.setStatusCode(HttpStatus.OK);
      return json;
    };
    return getAstronomicPictureUsecase.getAstronomicPicture()
      .apply(id)
      .map(eitherErrorOrAstronomicPicture -> ControllerResponses.jsonResponse(eitherErrorOrAstronomicPicture, failureResponse, successResponse));
  }
}
