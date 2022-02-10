package org.example.simple_sample_apis.r2dbc;

import java.util.ArrayList;
import reactor.test.StepVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.example.simple_sample_apis.r2dbc.models.DbCustomerImpl;
import org.example.simple_sample_apis.r2dbc.repositories.DbCustomersR2dbcRepository;

@SpringBootTest
public class DbCustomersR2dbcRepositoryTest {
  @Autowired
  private DbCustomersR2dbcRepository dbCustomersR2dbcRepository;

  @Test
  void autowiring() {
    assertNotNull(dbCustomersR2dbcRepository);
  }

  @Test
  void createSaveAndFindCustomerByName() {
    final var customerName = "test-createSaveAndFindCustomerByName";
    final var customerAge = 7;
    final var numberOfCustomers = 1;
    dbCustomersR2dbcRepository
      .save(new DbCustomerImpl(null, customerName, customerAge))
      .doOnNext(savedCustomer ->
        assertAll(
          () -> assertNotNull(savedCustomer),
          () -> assertNotNull(savedCustomer.getId()),
          () -> assertEquals(customerName, savedCustomer.getName()),
          () -> assertEquals(customerAge, savedCustomer.getAge())
        )
      )
      .flatMapMany(savedCustomer -> dbCustomersR2dbcRepository.findAllByName(customerName))
      .as(StepVerifier::create)
      .recordWith(ArrayList::new)
      .expectNextCount(numberOfCustomers)
      .consumeRecordedWith(dbCustomers -> {
        final var dbCustomersList = new ArrayList<>(dbCustomers);
        assertAll(
          () -> assertNotNull(dbCustomers),
          () -> assertEquals(numberOfCustomers, dbCustomers.size()),
          () -> assertEquals(customerName, dbCustomersList.get(0).getName()),
          () -> assertEquals(customerAge, dbCustomersList.get(0).getAge())
        );
      })
      .expectComplete()
      .log()
      .verify();
  }
}
