package org.example.simple_sample_apis.r2dbc.repositories;

import reactor.core.publisher.Flux;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.example.simple_sample_apis.r2dbc.models.DbCustomerImpl;

public interface DbCustomersR2dbcRepository extends ReactiveCrudRepository<DbCustomerImpl, Integer> {
  @Query("select id, c.name, c.age from customers c where c.name = $1")
  Flux<DbCustomerImpl> findAllByName(String name);

  @Query("select * from customers c where c.age = :age")
  Flux<DbCustomerImpl> findByAge(int age);
}
