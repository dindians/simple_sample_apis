package org.example.simple_sample_apis.usecases;

public class Customer{
  private final Integer id;
  private final String name;
  private final Integer age;

  public Customer(Integer id, String name, Integer age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public Integer getId() { return id; }
  public String getName() { return name; }
  public Integer getAge() { return age; }
}
