package org.example.simple_sample_apis.r2dbc;

import java.util.ArrayList;
import reactor.test.StepVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.simple_sample_apis.r2dbc.models.DbCustomerImpl;
import org.example.simple_sample_apis.r2dbc.repositories.DbCustomersR2dbcRepository;
import org.example.simple_sample_apis.r2dbc.repositories.DbCustomersRepository;
import org.example.simple_sample_apis.r2dbc.models.DbCustomer;

@SpringBootTest
public class DbCustomersRepositoryTest {
  @Autowired
  private DbCustomersR2dbcRepository dbCustomersR2dbcRepository;

  @Autowired
  private DbCustomersRepository dbCustomersRepository;

  @Test
  void autowiring() {
    assertAll(
      () -> assertNotNull(dbCustomersR2dbcRepository),
      () -> assertNotNull(dbCustomersRepository)
    );
  }

  @Test
  void getNonExistingDbCustomerById() {
    final var customerId = 13;
    dbCustomersRepository.getDbCustomerById(customerId)
      .as(StepVerifier::create)
      .expectNextCount(0)
      .expectComplete()
      .log()
      .verify();
  }

  @Test
  void createSaveAndGetDbCustomerById() {
    final var customerName = "test-createSaveAndGetDbCustomerById";
    final var customerAge = 954;
    dbCustomersRepository
      .saveDbCustomer(new DbCustomerImpl(null, customerName, customerAge))
      .map(savedDbCustomer -> {
        assertAll(
          () -> assertNotNull(savedDbCustomer),
          () -> assertNotNull(savedDbCustomer.getId()),
          () -> assertEquals(customerName, savedDbCustomer.getName()),
          () -> assertEquals(customerAge, savedDbCustomer.getAge())
        );
        return savedDbCustomer;
      })
      .flatMap(savedDbCustomer -> dbCustomersRepository.getDbCustomerById(savedDbCustomer.getId()))
      .as(StepVerifier::create)
      .assertNext(dbCustomer ->
        assertAll(
          () -> assertNotNull(dbCustomer),
          () -> assertEquals(customerName, dbCustomer.getName()),
          () -> assertEquals(customerAge, dbCustomer.getAge())
        )
      )
      .expectComplete()
      .log()
      .verify();
  }

  @Test
  void createSaveGetAndDeleteDbCustomerById() {
    final var customerName = "test-createSaveGetAndDeleteDbCustomerById";
    final var customerAge = 4711;
    dbCustomersRepository
      .saveDbCustomer(new DbCustomerImpl(null, customerName, customerAge))
      .map(savedCustomer -> {
        assertAll(
          () -> assertNotNull(savedCustomer),
          () -> assertNotNull(savedCustomer.getId()),
          () -> assertEquals(customerName, savedCustomer.getName()),
          () -> assertEquals(customerAge, savedCustomer.getAge())
        );
        return savedCustomer;
      })
      .flatMap(savedCustomer ->  dbCustomersRepository.getDbCustomerById(savedCustomer.getId()))
      .map(dbCustomer -> {
        assertAll(
          () -> assertNotNull(dbCustomer),
          () -> assertEquals(customerName, dbCustomer.getName()),
          () -> assertEquals(customerAge, dbCustomer.getAge())
        );
        return dbCustomer;
      })
      .flatMap(dbCustomer -> dbCustomersR2dbcRepository.deleteById(dbCustomer.getId()))
      .as(StepVerifier::create)
      .expectComplete()
      .log()
      .verify();
  }

  @Test
  void createSaveAndGetDbCustomersByAge() {
    final var customerName = "test-createSaveAndGetDbCustomersByAge";
    final var customerName2 = "createSaveAndGetDbCustomersByAge-other";
    final var customerAge = 42;
    final var dbCustomersToSave = new ArrayList<DbCustomer>();
    dbCustomersToSave.add(new DbCustomerImpl(null, customerName, customerAge));
    dbCustomersToSave.add(new DbCustomerImpl(null, customerName2, customerAge));
    dbCustomersRepository
      .saveDbCustomers(dbCustomersToSave)
      .map(savedCustomer -> {
        assertAll(
          () -> assertNotNull(savedCustomer),
          () -> assertEquals(customerAge, savedCustomer.getAge())
        );
        return savedCustomer;
      })
      .count()
      .doOnNext(numberOfSaveRecords -> assertEquals(dbCustomersToSave.size(), numberOfSaveRecords))
      .flatMapMany(numberOfSaveRecords -> dbCustomersRepository.getDbCustomersByAge(customerAge))
      .as(StepVerifier::create)
      .recordWith(ArrayList::new)
      .expectNextCount(dbCustomersToSave.size())
      .consumeRecordedWith(dbCustomers -> {
        final var dbCustomersList = new ArrayList<>(dbCustomers);
        assertAll(
          () -> assertNotNull(dbCustomers),
          () -> assertEquals(dbCustomersToSave.size(), dbCustomers.size()),
          () -> assertEquals(customerAge, dbCustomersList.get(0).getAge()),
          () -> assertEquals(customerAge, dbCustomersList.get(1).getAge())
        );
      })
      .expectComplete()
      .log()
      .verify();
  }
}
