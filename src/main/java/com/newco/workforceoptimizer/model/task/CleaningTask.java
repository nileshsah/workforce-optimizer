package com.newco.workforceoptimizer.model.task;

import com.newco.workforceoptimizer.model.cleaner.Cleaner;
import lombok.Data;

import java.util.List;

@Data
public class CleaningTask {
  private Integer roomsToClean;
  private List<Cleaner> availableCleaners;
}
