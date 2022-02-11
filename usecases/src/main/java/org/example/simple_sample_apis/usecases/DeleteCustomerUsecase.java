package org.example.simple_sample_apis.usecases;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;
import org.example.simple_sample_apis.fp.Unit;

public class DeleteCustomerUsecase {
  private final Function<Integer, Mono<Either<EitherError, Unit>>> deleteCustomer;

  public DeleteCustomerUsecase(@NotNull Function<Integer, Mono<Either<EitherError, Unit>>> deleteCustomer) {
    this.deleteCustomer = deleteCustomer;
  }

  public Function<Integer, Mono<Either<EitherError, Unit>>> deleteCustomer() { return deleteCustomer; }
}
