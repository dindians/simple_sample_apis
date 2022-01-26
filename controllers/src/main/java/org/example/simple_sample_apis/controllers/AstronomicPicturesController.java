package org.example.simple_sample_apis.controllers;

import org.example.simple_sample_apis.usecase_factories.AstronomicPictureUsecaseFactory;
import org.example.simple_sample_apis.usecase_factories.Serializer;
import org.example.simple_sample_apis.usecases.GetAstronomicPictureUsecase;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import static org.example.simple_sample_apis.controllers.HttpStatusFromEitherError.httpStatusFromEitherError;

@RestController
@RequestMapping(value = "/astronomic-pictures", produces = MediaType.APPLICATION_JSON_VALUE)
public class AstronomicPicturesController {
  private final GetAstronomicPictureUsecase getAstronomicPictureUsecase;

  public AstronomicPicturesController(@NotNull AstronomicPictureUsecaseFactory astronomicPictureUsecaseFactory) {
    getAstronomicPictureUsecase = astronomicPictureUsecaseFactory.getAstronomicPictureUsecase();
  }

  @GetMapping("/{id}")
  public Mono<String> getAstronomicPicture(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable long id) {
    return getAstronomicPictureUsecase.getAstronomicPicture().apply(id)
      .map(eitherErrorOrAstronomicPicture ->
        eitherErrorOrAstronomicPicture
          .flatMap(Serializer::serialize)
          .fold(
            eitherError -> {
              serverHttpResponse.setStatusCode(httpStatusFromEitherError(eitherError));
              return ErrorHandling.error(eitherError, String.format("rest-controller-class: %s; path: /astronomic-pictures/%d", AstronomicPicturesController.class.getName(), id), AstronomicPicturesController.class);
            },
            astronomicPictureJson -> {
              serverHttpResponse.setStatusCode(HttpStatus.OK);
              return astronomicPictureJson;
            }
          )
      );
  }
}
