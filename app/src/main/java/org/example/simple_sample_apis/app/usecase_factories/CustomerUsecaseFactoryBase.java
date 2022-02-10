package org.example.simple_sample_apis.app.usecase_factories;

import org.example.simple_sample_apis.r2dbc.models.DbCustomer;
import org.example.simple_sample_apis.r2dbc.models.DbCustomerImpl;
import org.example.simple_sample_apis.r2dbc.repositories.DbCustomersRepository;
import org.example.simple_sample_apis.usecases.Customer;
import org.jetbrains.annotations.NotNull;

public abstract class CustomerUsecaseFactoryBase {
  static final class EmptyDbCustomer implements DbCustomer {
    @Override
    public Integer getId() { return null; }
    @Override
    public String getName() { return null; }
    @Override
    public Integer getAge() { return null; }
  }

  protected final DbCustomersRepository dbCustomersRepository;

  public CustomerUsecaseFactoryBase(@NotNull DbCustomersRepository dbCustomersRepository) {
    this.dbCustomersRepository = dbCustomersRepository;
  }

  protected Boolean isNotEmpty(@NotNull DbCustomer dbCustomer) { return !(dbCustomer instanceof EmptyDbCustomer); }
  protected DbCustomer customerToDbCustomer(@NotNull Customer customer) { return new DbCustomerImpl(customer.getId(), customer.getName(), customer.getAge()); }
  protected Customer newCustomer(@NotNull DbCustomer dbCustomer) { return  new Customer(dbCustomer.getId(), dbCustomer.getName(), dbCustomer.getAge()); }
}
