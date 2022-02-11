package org.example.simple_sample_apis.r2dbc.repositories;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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

  public Mono<DbCustomer> saveDbCustomer(@NotNull DbCustomer dbCustomer) {
    return dbCustomersR2dbcRepository.save(dbCustomerToDbCustomerImpl(dbCustomer)).map(this::dbCustomerImplToDbCustomer);
  }

  public Flux<DbCustomer> saveDbCustomers(@NotNull Iterable<DbCustomer> dbCustomers) {
    return dbCustomersR2dbcRepository.saveAll(StreamSupport.stream(dbCustomers.spliterator(), false).map(this::dbCustomerToDbCustomerImpl).collect(Collectors.toList())).map(this::dbCustomerImplToDbCustomer);
  }

  public Mono<Void> deleteDbCustomerById(Integer customerId) {
    return dbCustomersR2dbcRepository.deleteById(customerId);
  }

  public Mono<DbCustomer> getDbCustomerById(Integer customerId) {
    return dbCustomersR2dbcRepository.findById(customerId).map(this::dbCustomerImplToDbCustomer);
  }

  public Flux<DbCustomer> getDbCustomersByAge(Integer age) { return dbCustomersR2dbcRepository.findByAge(age).map(this::dbCustomerImplToDbCustomer); }

  private DbCustomerImpl dbCustomerToDbCustomerImpl(@NotNull DbCustomer dbCustomer) { return new DbCustomerImpl(dbCustomer.getId(), dbCustomer.getName(), dbCustomer.getAge()); }
  private DbCustomer dbCustomerImplToDbCustomer(@NotNull DbCustomerImpl dbCustomerImpl) { return dbCustomerImpl; }
}
