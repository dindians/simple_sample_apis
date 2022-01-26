package org.example.simple_sample_apis.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example.simple_sample_apis.controllers", "org.example.simple_sample_apis.usecase_factories"})
public class RestServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(RestServiceApplication.class, args);
  }
}
