package org.example.simple_sample_apis.usecase_factories;

import org.jetbrains.annotations.NotNull;

public interface ConfigValues {
  String get(@NotNull String name);
}
