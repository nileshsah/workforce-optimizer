package com.newco.workforceoptimizer.model.task;

import com.newco.workforceoptimizer.model.cleaner.Cleaner;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class CleaningTaskResult {
  private final Map<Cleaner, Integer> optimizedCapacityMap;

  public Integer capacityOf(Cleaner cleaner) {
    return optimizedCapacityMap.get(cleaner);
  }

}
