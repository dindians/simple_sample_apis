package org.example.simple_sample_apis.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.example.simple_sample_apis.rest_controllers.controllers.*;

@SpringBootTest(properties = "spring.main.web-application-type=reactive")
class SmokeTest {
  @Autowired
  private GetPersonsController personController;
  @Autowired
  private GetAstronomicPicturesController getAstronomicPicturesController;
  @Autowired
  private GetPersonsWithPictureController getPersonsWithPictureController;
  @Autowired
  private GetCustomersController getCustomersController;
  @Autowired
  private DeleteCustomerController deleteCustomerController;
  @Autowired
  private PostCustomerController postCustomerController;

  @Test
  void loadAllControllers() {
    assertAll(
      () -> assertNotNull(this.personController),
      () -> assertNotNull(this.getAstronomicPicturesController),
      () -> assertNotNull(this.getPersonsWithPictureController),
      () -> assertNotNull(this.getCustomersController),
      () -> assertNotNull(this.deleteCustomerController),
      () -> assertNotNull(this.postCustomerController)
    );
  }
}
