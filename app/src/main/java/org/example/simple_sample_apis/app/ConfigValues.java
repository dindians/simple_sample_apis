package org.example.simple_sample_apis.app;

import org.jetbrains.annotations.NotNull;

public interface ConfigValues {
  String get(@NotNull String name);
}
