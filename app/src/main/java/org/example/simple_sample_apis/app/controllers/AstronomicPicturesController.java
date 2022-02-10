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
import org.example.simple_sample_apis.app.usecase_factories.AstronomicPictureUsecaseFactory;
import org.example.simple_sample_apis.usecases.GetAstronomicPictureUsecase;
import static org.example.simple_sample_apis.app.controllers.CreateHttpStatus.httpStatusFromEitherError;

@RestController
@RequestMapping(value = "/astronomic-pictures", produces = MediaType.APPLICATION_JSON_VALUE)
public final class AstronomicPicturesController {
  private final GetAstronomicPictureUsecase getAstronomicPictureUsecase;

  public AstronomicPicturesController(@NotNull AstronomicPictureUsecaseFactory astronomicPictureUsecaseFactory) {
    getAstronomicPictureUsecase = astronomicPictureUsecaseFactory.getAstronomicPictureUsecase();
  }

  @GetMapping("/{id}")
  public Mono<String> getAstronomicPicture(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable long id) {
    Function<EitherError, String> failureResponse = eitherError -> {
      serverHttpResponse.setStatusCode(httpStatusFromEitherError(eitherError));
      return EitherErrorToJson.execute(eitherError,String.format("rest-controller-class: %s; path: /persons/%d", PersonsController.class.getName(), id), AstronomicPicturesController.class);
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
