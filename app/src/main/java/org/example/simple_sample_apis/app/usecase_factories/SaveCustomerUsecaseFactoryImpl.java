package org.example.simple_sample_apis.app.usecase_factories;

import java.util.function.Function;

import org.example.simple_sample_apis.rest_controllers.usecase_factories.SaveCustomerUsecaseFactory;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.fp.EntityNotSaved;
import org.example.simple_sample_apis.r2dbc.models.DbCustomer;
import org.example.simple_sample_apis.r2dbc.repositories.DbCustomersRepository;
import org.example.simple_sample_apis.usecases.Customer;
import org.example.simple_sample_apis.usecases.SaveCustomerUsecase;

@Component
public final class SaveCustomerUsecaseFactoryImpl extends CustomerUsecaseFactoryBase implements SaveCustomerUsecaseFactory {
  public SaveCustomerUsecaseFactoryImpl(@NotNull DbCustomersRepository dbCustomersRepository) { super(dbCustomersRepository); }

  @Override
  public SaveCustomerUsecase saveCustomerUsecase() { return new SaveCustomerUsecase(this::saveCustomer); }

  private Mono<Either<EitherError, Customer>> saveCustomer(@NotNull Customer customer) {
    final class SaveCustomerError implements EitherError {
      private final Customer customer;
      private final String errorText;
      private final Throwable exception;

      SaveCustomerError(@NotNull Customer customer, String errorText, Throwable exception) {
        this.customer = customer;
        this.errorText = errorText;
        this.exception = exception;
      }

      public Customer getCustomer() { return customer; }
      public String getErrorText() { return errorText; }
      public Throwable getException() { return exception; }
    }
    final class CustomerNotSaved implements EntityNotSaved {
      private final Customer customer;

      CustomerNotSaved(@NotNull Customer customerId) {
        this.customer = customerId;
      }
      public Customer getCustomer() { return customer; }
    }

    Function<DbCustomer, Either<EitherError,Customer>> dbCustomerToCustomer = dbCustomer -> isNotEmpty(dbCustomer) ? Either.right(newCustomer(dbCustomer)) : Either.left(new CustomerNotSaved(customer));

    try {
      return dbCustomersRepository.saveDbCustomer(customerToDbCustomer(customer)).defaultIfEmpty(new GetCustomerUsecaseFactoryImpl.EmptyDbCustomer()).map(dbCustomerToCustomer);
    }
    catch(Exception exception) {
      return Mono.just(Either.left(new SaveCustomerError(customer, String.format("an error of type %s has occurred ", exception.getClass().getName()), exception)));
    }
  }
}
