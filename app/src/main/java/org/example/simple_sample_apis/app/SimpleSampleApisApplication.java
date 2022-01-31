package org.example.simple_sample_apis.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "org.example.simple_sample_apis.app", "org.example.simple_sample_apis.app.usecase_factories", "org.example.simple_sample_apis.http_client" })
public class SimpleSampleApisApplication {
  public static void main(String[] args) {
    SpringApplication.run(SimpleSampleApisApplication.class, args);
  }
}
