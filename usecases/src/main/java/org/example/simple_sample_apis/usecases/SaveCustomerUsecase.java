package org.example.simple_sample_apis.usecases;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;

public class SaveCustomerUsecase {
  private final Function<Customer, Mono<Either<EitherError, Customer>>> saveCustomer;

  public SaveCustomerUsecase(@NotNull Function<Customer, Mono<Either<EitherError, Customer>>> saveCustomer) {
    this.saveCustomer = saveCustomer;
  }

  public Function<Customer, Mono<Either<EitherError, Customer>>> saveCustomer() { return saveCustomer; }
}
