package com.newco.workforceoptimizer.model.task;

import com.newco.workforceoptimizer.model.cleaner.Cleaner;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CleaningTask {
  private final Integer roomsToClean;
  private final List<Cleaner> availableCleaners;
}
