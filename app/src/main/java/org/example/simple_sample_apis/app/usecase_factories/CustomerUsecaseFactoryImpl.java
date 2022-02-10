package org.example.simple_sample_apis.app.usecase_factories;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.r2dbc.models.DbCustomer;
import org.example.simple_sample_apis.r2dbc.repositories.DbCustomersRepository;
import org.example.simple_sample_apis.usecases.Customer;
import org.example.simple_sample_apis.usecases.GetCustomerUsecase;
import org.example.simple_sample_apis.fp.NotFoundError;

@Component
public final class CustomerUsecaseFactoryImpl implements CustomerUsecaseFactory {
  private final DbCustomersRepository dbCustomersRepository;

  public CustomerUsecaseFactoryImpl(@NotNull DbCustomersRepository dbCustomersRepository) {
    this.dbCustomersRepository = dbCustomersRepository;
  }

  @Override
  public GetCustomerUsecase getCustomerUsecase() { return new GetCustomerUsecase(this::getCustomer); }

  private Mono<Either<EitherError, Customer>> getCustomer(Integer customerId) {
    final class DbCustomerNotFound implements NotFoundError {
      private final Integer customerId;

      DbCustomerNotFound(Integer customerId) {
        this.customerId = customerId;
      }
      public Integer getCustomerId() { return customerId; }
    }

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

    final class EmptyDbCustomer implements DbCustomer {
      @Override
      public Integer getId() { return null; }
      @Override
      public String getName() { return null; }
      @Override
      public Integer getAge() { return null; }
    }

    Function<DbCustomer, Boolean> isNotEmpty = dbCustomer -> !(dbCustomer instanceof EmptyDbCustomer);
    Function<DbCustomer, Customer> newCustomer = dbCustomer -> new Customer(dbCustomer.getId(), dbCustomer.getName(), dbCustomer.getAge());
    Function<DbCustomer, Either<EitherError,Customer>> dbCustomerToCustomer = dbCustomer -> isNotEmpty.apply(dbCustomer) ? Either.right(newCustomer.apply(dbCustomer)) : Either.left(new DbCustomerNotFound(customerId));

    try {
      return dbCustomersRepository.getDbCustomerById(customerId).defaultIfEmpty(new EmptyDbCustomer()).map(dbCustomerToCustomer);
    }
    catch(Exception exception) {
      return Mono.just(Either.left(new GetCustomerError(customerId, String.format("an error of type %s has occurred ", exception.getClass().getName()), exception)));
    }
  }
}
