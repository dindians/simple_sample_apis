package org.example.simple_sample_apis.app.usecase_factories;

import java.util.function.Function;

import org.example.simple_sample_apis.rest_controllers.usecase_factories.GetCustomerUsecaseFactory;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.fp.EntityNotFound;
import org.example.simple_sample_apis.r2dbc.models.DbCustomer;
import org.example.simple_sample_apis.r2dbc.repositories.DbCustomersRepository;
import org.example.simple_sample_apis.usecases.Customer;
import org.example.simple_sample_apis.usecases.GetCustomerUsecase;

@Component
public final class GetCustomerUsecaseFactoryImpl extends CustomerUsecaseFactoryBase implements GetCustomerUsecaseFactory {
  public GetCustomerUsecaseFactoryImpl(@NotNull DbCustomersRepository dbCustomersRepository) { super(dbCustomersRepository); }

  @Override
  public GetCustomerUsecase getCustomerUsecase() { return new GetCustomerUsecase(this::getCustomer); }

  private Mono<Either<EitherError, Customer>> getCustomer(Integer customerId) {
    final class GetCustomerError implements EitherError {
      private final Integer customerId;
      private final String errorText;
      private final Throwable exception;

      GetCustomerError(Integer customerId, String errorText, Throwable exception) {
        this.customerId = customerId;
        this.errorText = errorText;
        this.exception = exception;
      }

      public Integer getCustomerId() { return customerId; }
      public String getErrorText() { return errorText; }
      public Throwable getException() { return exception; }
    }
    final class DbCustomerNotFound implements EntityNotFound {
      private final Integer customerId;

      DbCustomerNotFound(Integer customerId) {
        this.customerId = customerId;
      }
      public Integer getCustomerId() { return customerId; }
    }

    Function<DbCustomer, Either<EitherError,Customer>> dbCustomerToCustomer = dbCustomer -> isNotEmpty(dbCustomer) ? Either.right(newCustomer(dbCustomer)) : Either.left(new DbCustomerNotFound(customerId));

    try {
      return dbCustomersRepository.getDbCustomerById(customerId).defaultIfEmpty(new EmptyDbCustomer()).map(dbCustomerToCustomer);
    }
    catch(Exception exception) {
      return Mono.just(Either.left(new GetCustomerError(customerId, String.format("an error of type %s has occurred ", exception.getClass().getName()), exception)));
    }
  }
}
