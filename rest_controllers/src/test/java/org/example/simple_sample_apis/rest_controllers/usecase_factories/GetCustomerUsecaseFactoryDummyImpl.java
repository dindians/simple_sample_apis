package org.example.simple_sample_apis.rest_controllers.usecase_factories;

import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.rest_controllers.usecase_factories.GetCustomerUsecaseFactory;
import org.example.simple_sample_apis.usecases.Customer;
import org.example.simple_sample_apis.usecases.GetCustomerUsecase;

@Component
public class GetCustomerUsecaseFactoryDummyImpl implements GetCustomerUsecaseFactory {
  @Override
  public GetCustomerUsecase getCustomerUsecase() {
    return new GetCustomerUsecase(this::getCustomer);
  }

  private Mono<Either<EitherError, Customer>> getCustomer(Integer customerId) {
    return Mono.just(Either.right(new Customer(customerId, "a", 3)));
  }
}
