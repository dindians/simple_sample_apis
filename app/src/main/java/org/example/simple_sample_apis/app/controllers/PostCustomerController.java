package org.example.simple_sample_apis.app.controllers;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.app.usecase_factories.SaveCustomerUsecaseFactory;
import org.example.simple_sample_apis.usecases.SaveCustomerUsecase;
import org.example.simple_sample_apis.usecases.Customer;
import static org.example.simple_sample_apis.app.controllers.CreateHttpStatus.httpStatusFromEitherError;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public final class PostCustomerController {
  private final SaveCustomerUsecase saveCustomerUsecase;
  private static final class NewCustomer {
    private final String name;
    private final Integer age;

    private NewCustomer(String name, Integer age) {
      this.name = name;
      this.age = age;
    }

    public String getName() { return name; }
    public Integer getAge() { return age; }
  }

  public PostCustomerController(@NotNull SaveCustomerUsecaseFactory saveCustomerUsecaseFactory) { saveCustomerUsecase = saveCustomerUsecaseFactory.saveCustomerUsecase(); }

  @PostMapping
  public Mono<String> postCustomer(@NotNull ServerHttpResponse serverHttpResponse, @RequestBody Mono<NewCustomer> monoNewCustomer) {
    Function<EitherError, String> failureResponse = eitherError -> {
      serverHttpResponse.setStatusCode(httpStatusFromEitherError(eitherError));
      return EitherErrorToJson.execute(eitherError, String.format("rest-controller-class: %s; method: POST; path: /customers", PostCustomerController.class.getName()), PostCustomerController.class);
    };
    Function<String, String> successResponse = json -> {
      serverHttpResponse.setStatusCode(HttpStatus.OK);
      return json;
    };

    Function <NewCustomer, Customer> newCustomerToCustomer = newCustomer -> new Customer(null, newCustomer.getName(), newCustomer.getAge());

    return monoNewCustomer
      .flatMap(newCustomer -> saveCustomerUsecase.saveCustomer().apply(newCustomerToCustomer.apply(newCustomer)))
      .map(eitherErrorOrCustomer -> ControllerResponses.jsonResponse(eitherErrorOrCustomer, failureResponse, successResponse));
  }
}
