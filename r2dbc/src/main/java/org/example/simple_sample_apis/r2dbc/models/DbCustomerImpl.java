package org.example.simple_sample_apis.r2dbc.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Customers")
public class DbCustomerImpl implements DbCustomer {
  @Id
  private final Integer id;
  private final String name;
  private final Integer age;

   public DbCustomerImpl(Integer id, String name, Integer age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  @Override
  public Integer getId() { return id; }
  @Override
  public String getName() { return name; }
  @Override
  public Integer getAge() { return age; }
}
