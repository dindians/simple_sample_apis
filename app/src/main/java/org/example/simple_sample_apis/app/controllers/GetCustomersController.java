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
import org.example.simple_sample_apis.app.usecase_factories.GetCustomerUsecaseFactory;
import org.example.simple_sample_apis.usecases.GetCustomerUsecase;
import static org.example.simple_sample_apis.app.controllers.CreateHttpStatus.httpStatusFromEitherError;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public final class GetCustomersController {
  private final GetCustomerUsecase getCustomerUsecase;

  public GetCustomersController(@NotNull GetCustomerUsecaseFactory getCustomerUsecaseFactory) { getCustomerUsecase = getCustomerUsecaseFactory.getCustomerUsecase(); }

  @GetMapping("/{customerId}")
  public Mono<String> getCustomer(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable Integer customerId) {
    Function<EitherError, String> failureResponse = eitherError -> {
      serverHttpResponse.setStatusCode(httpStatusFromEitherError(eitherError));
      return EitherErrorToJson.execute(eitherError, String.format("rest-controller-class: %s; method: GET; path: /customers/%d", GetCustomersController.class.getName(), customerId), GetCustomersController.class);
    };
    Function<String, String> successResponse = json -> {
      serverHttpResponse.setStatusCode(HttpStatus.OK);
      return json;
    };
    return getCustomerUsecase.getCustomer()
      .apply(customerId)
      .map(eitherErrorPerson -> ControllerResponses.jsonResponse(eitherErrorPerson, failureResponse, successResponse));
  }
}
