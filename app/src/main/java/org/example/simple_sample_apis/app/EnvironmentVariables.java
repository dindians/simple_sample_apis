package org.example.simple_sample_apis.app;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
final class EnvironmentVariables implements ConfigValues {
  private final Environment environment;

  EnvironmentVariables(Environment environment) {
    this.environment = environment;
  }

  @Override
  public String get(@NotNull String name) {  return environment.getProperty(name); }
}
