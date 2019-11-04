package com.newco.workforceoptimizer.model.cleaner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Cleaner {
  private final CleanerType type;
  private final Integer cleaningCapacity;
}
