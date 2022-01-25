package org.example.simple_sample_apis.services;

public final class PersonFromMemory {
  private final long id;

  public PersonFromMemory(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }
}
