package com.newco.workforceoptimizer.model.cleaner;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Cleaner {
  private final CleanerType type;
  private final Integer cleaningCapacity;
}
