package org.example.simple_sample_apis.usecases;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.fp.EitherError;

public final class GetCustomerUsecase {
  private final Function<Integer, Mono<Either<EitherError, Customer>>> getCustomer;

  public GetCustomerUsecase(@NotNull Function<Integer, Mono<Either<EitherError, Customer>>> getCustomer) { this.getCustomer = getCustomer; }

  public Function<Integer, Mono<Either<EitherError, Customer>>> getCustomer() { return getCustomer; }
}
