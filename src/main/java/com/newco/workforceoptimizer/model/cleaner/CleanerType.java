package com.newco.workforceoptimizer.model.cleaner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CleanerType {
  SENIOR("senior"),
  JUNIOR("junior");

  private final String name;
}
