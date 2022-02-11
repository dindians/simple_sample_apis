package org.example.simple_sample_apis.rest_controllers.usecase_factories;

import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.fp.Unit;
import org.example.simple_sample_apis.usecases.DeleteCustomerUsecase;

@Component
public class DeleteCustomerUsecaseFactoryMock implements DeleteCustomerUsecaseFactory {
  @Override
  public DeleteCustomerUsecase deleteCustomerUSecase() {
    return new DeleteCustomerUsecase(this::deleteCustomerById);
  }

  private Mono<Either<EitherError, Unit>> deleteCustomerById(Integer customerId) {
    return Mono.just(Either.right(Unit.UNIT));
  }
}
