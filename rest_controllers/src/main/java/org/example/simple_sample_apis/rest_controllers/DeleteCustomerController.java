package org.example.simple_sample_apis.rest_controllers;

import java.util.function.Function;

import org.example.simple_sample_apis.fp.Unit;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.usecases.DeleteCustomerUsecase;
import static org.example.simple_sample_apis.rest_controllers.CreateHttpStatus.httpStatusFromEitherError;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeleteCustomerController {
  private final DeleteCustomerUsecase deleteCustomerUsecase;

  public DeleteCustomerController(@NotNull DeleteCustomerUsecaseFactory deleteCustomerUsecaseFactory) { deleteCustomerUsecase = deleteCustomerUsecaseFactory.deleteCustomerUSecase(); }

  @DeleteMapping("/{customerId}")
  public Mono<String> deleteCustomer(@NotNull ServerHttpResponse serverHttpResponse, @PathVariable Integer customerId) {
    Function<EitherError, String> failureResponse = eitherError -> {
      serverHttpResponse.setStatusCode(httpStatusFromEitherError(eitherError));
      return EitherErrorToJson.execute(eitherError, String.format("rest-controller-class: %s; method: POST; path: /customers", DeleteCustomerController.class.getName()), DeleteCustomerController.class);
    };
    Function<String, String> successResponse = json -> {
      serverHttpResponse.setStatusCode(HttpStatus.NO_CONTENT);
      return json;
    };

    return deleteCustomerUsecase.deleteCustomer().apply(customerId)
      .map(eitherErrorOrUnit -> ControllerResponses.jsonResponse(eitherErrorOrUnit, failureResponse, successResponse));
  }
}
