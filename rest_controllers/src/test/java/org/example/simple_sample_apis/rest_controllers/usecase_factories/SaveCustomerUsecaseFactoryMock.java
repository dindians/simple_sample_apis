package org.example.simple_sample_apis.rest_controllers.usecase_factories;

import org.example.simple_sample_apis.usecases.SaveCustomerUsecase;
import org.springframework.stereotype.Component;

@Component
public class SaveCustomerUsecaseFactoryMock implements SaveCustomerUsecaseFactory {
  @Override
  public SaveCustomerUsecase saveCustomerUsecase() {
    return null;
  }
}
