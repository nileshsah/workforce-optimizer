package com.newco.workforceoptimizer.model.cleaner;

import lombok.Data;

@Data
public class Cleaner {
  private CleanerType type;
  private Integer cleaningCapacity;
}
