package org.example.simple_sample_apis.usecases;

public final class PersonWithPicture extends Person {
  private final String picture;
  private final String title;
  private final String description;

  public PersonWithPicture(long id, String picture, String title, String description) {
    super(id);
    this.picture = picture;
    this.title = title;
    this.description = description;
  }

  public String getPicture() { return picture; }
  public String getTitle() { return title; }
  public String getDescription() { return description; }
}
