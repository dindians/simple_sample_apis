package org.example.simple_sample_apis.app.usecase_factories;

import org.example.simple_sample_apis.usecases.GetPersonUsecase;

public interface PersonUsecaseFactory {
  GetPersonUsecase getPersonUsecase();
}
