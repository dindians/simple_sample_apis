package org.example.simple_sample_apis.r2dbc.repositories;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.example.simple_sample_apis.r2dbc.models.DbCustomer;
import org.example.simple_sample_apis.r2dbc.models.DbCustomerImpl;

/*
wrapper class around the interface DbCustomersR2dbcRepository to abstract away the dependency on DbCustomersR2dbcRepository which extends ReactiveCrudRepository<T, ID> interface defined in spring data r2dbc library.
 */
@Component
public class DbCustomersRepository {
  private final DbCustomersR2dbcRepository dbCustomersR2dbcRepository;

  public DbCustomersRepository(DbCustomersR2dbcRepository dbCustomersR2dbcRepository) {
    this.dbCustomersR2dbcRepository = dbCustomersR2dbcRepository;
  }

  public Mono<DbCustomer> getDbCustomerById(Integer customerId) {
    // todo remove the creation and save of a new DbCustomerImpl.
    return dbCustomersR2dbcRepository.save(new DbCustomerImpl(null, String.format("a-brand-new-customer[%d]", customerId), 1000 + customerId)).flatMap(savedDbCustomerImpl -> dbCustomersR2dbcRepository.findById(customerId));
 //   return dbCustomersR2dbcRepository.findById(customerId).map(this::dbCustomerImplToDbCustomer);
  }

  public Flux<DbCustomer> getDbCustomersByAge(Integer age) { return dbCustomersR2dbcRepository.findByAge(age).map(this::dbCustomerImplToDbCustomer); }

  private DbCustomer dbCustomerImplToDbCustomer(@NotNull DbCustomerImpl dbCustomerImpl) { return dbCustomerImpl; }
}
