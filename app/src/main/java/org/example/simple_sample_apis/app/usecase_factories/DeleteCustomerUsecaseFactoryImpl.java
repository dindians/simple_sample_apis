package org.example.simple_sample_apis.app.usecase_factories;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.fp.Unit;
import org.example.simple_sample_apis.r2dbc.repositories.DbCustomersRepository;
import org.example.simple_sample_apis.rest_controllers.usecase_factories.DeleteCustomerUsecaseFactory;
import org.example.simple_sample_apis.usecases.DeleteCustomerUsecase;

@Component
public class DeleteCustomerUsecaseFactoryImpl extends CustomerUsecaseFactoryBase implements DeleteCustomerUsecaseFactory {
  public DeleteCustomerUsecaseFactoryImpl(@NotNull DbCustomersRepository dbCustomersRepository) { super(dbCustomersRepository); }

  @Override
  public DeleteCustomerUsecase deleteCustomerUSecase() {
    return new DeleteCustomerUsecase(this::deleteCustomerById);
  }

  private Mono<Either<EitherError, Unit>> deleteCustomerById(Integer customerId) {
    final class DeleteCustomerError implements EitherError {
      private final Integer customerId;
      private final String errorText;
      private final Throwable exception;

      DeleteCustomerError(Integer customerId, String errorText, Throwable exception) {
        this.customerId = customerId;
        this.errorText = errorText;
        this.exception = exception;
      }

      public Integer getCustomerId() { return customerId; }
      public String getErrorText() { return errorText; }
      public Throwable getException() { return exception; }
    }
    try {
      //todo how to intercept errors? E.g. in the case no customer could be deleted?
      return  dbCustomersRepository.deleteDbCustomerById(customerId).map(voyd -> Either.right(Unit.UNIT));
    }
    catch(Exception exception) {
      return Mono.just(Either.left(new DeleteCustomerError(customerId, String.format("an error of type %s has occurred ", exception.getClass().getName()), exception)));
    }
  }
}
