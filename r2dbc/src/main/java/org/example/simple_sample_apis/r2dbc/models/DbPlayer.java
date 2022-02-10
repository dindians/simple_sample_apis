package org.example.simple_sample_apis.r2dbc.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("players")
public class DbPlayer {
  @Id
  private final Integer id;
  private final String name;
  private final Integer age;

  public DbPlayer(Integer id, String name, Integer age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public Integer getId() { return id; }
  public String getName() { return name; }
  public Integer getAge() { return age; }
}
