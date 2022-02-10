package org.example.simple_sample_apis.app.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "spring.main.web-application-type=reactive")
class SmokeTest {
  @Autowired
  private GetPersonsController personController;
  @Autowired
  private GetAstronomicPicturesController getAstronomicPicturesController;
  @Autowired
  private GetPersonsWithPictureController getPersonsWithPictureController;

  @Test
  void loadAllControllers() {
    assertAll(
      () -> assertNotNull(this.personController),
      () -> assertNotNull(this.getAstronomicPicturesController),
      () -> assertNotNull(this.getPersonsWithPictureController)
    );
  }
}
