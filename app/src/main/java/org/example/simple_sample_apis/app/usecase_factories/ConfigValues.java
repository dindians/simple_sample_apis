package org.example.simple_sample_apis.app.usecase_factories;

import org.jetbrains.annotations.NotNull;

public interface ConfigValues {
  String get(@NotNull String name);
}
