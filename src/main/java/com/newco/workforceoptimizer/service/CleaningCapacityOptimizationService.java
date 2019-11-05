package com.newco.workforceoptimizer.service;

import com.newco.workforceoptimizer.model.cleaner.Cleaner;
import com.newco.workforceoptimizer.model.cleaner.CleanerType;
import com.newco.workforceoptimizer.model.task.CleaningTask;
import com.newco.workforceoptimizer.model.task.CleaningTaskResult;
import com.newco.workforceoptimizer.service.exception.InvalidTaskException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CleaningCapacityOptimizationService
    implements CapacityOptimizationService<CleaningTask, CleaningTaskResult> {

  private static final Integer INFINITY = (Integer.MAX_VALUE / 2) - 1;

  @Override
  public CleaningTaskResult optimize(CleaningTask task) {
    this.validateCleaningTask(task);

    Cleaner seniorCleaner =
        task.getAvailableCleaners().stream()
            .filter(cleaner -> cleaner.getType() == CleanerType.SENIOR)
            .findFirst()
            .get();
    Cleaner juniorCleaner =
        task.getAvailableCleaners().stream()
            .filter(cleaner -> cleaner.getType() == CleanerType.JUNIOR)
            .findFirst()
            .get();

    return calculateMinimumCapacity(task.getRoomsToClean(), seniorCleaner, juniorCleaner);
  }

  private CleaningTaskResult calculateMinimumCapacity(
      Integer roomsToClean, Cleaner seniorCleaner, Cleaner juniorCleaner) {

    Map<Cleaner, Integer> optimizedCleanerCapacity =
        new HashMap<Cleaner, Integer>() {
          {
            put(seniorCleaner, INFINITY);
            put(juniorCleaner, INFINITY);
          }
        };

    final Integer maximumSeniorCapacity = 1 + (roomsToClean / seniorCleaner.getCleaningCapacity());

    for(Integer seniorCleanerCount = 1; seniorCleanerCount <= maximumSeniorCapacity; seniorCleanerCount++) {
      Integer remainingRoomsToClean =
          Math.max(0, roomsToClean - (seniorCleanerCount * seniorCleaner.getCleaningCapacity()));
      Integer juniorCleanerCount =
          (int) Math.ceil((double) remainingRoomsToClean / juniorCleaner.getCleaningCapacity());

      Integer optimizedCapacity = optimizedCleanerCapacity.entrySet().stream()
          .mapToInt(Map.Entry::getValue)
          .sum();
      Integer currentCapacity = seniorCleanerCount + juniorCleanerCount;

      if (currentCapacity < optimizedCapacity) {
        optimizedCleanerCapacity.put(seniorCleaner, seniorCleanerCount);
        optimizedCleanerCapacity.put(juniorCleaner, juniorCleanerCount);
      }
    }

    return new CleaningTaskResult(optimizedCleanerCapacity);
  }

  private void validateCleaningTask(CleaningTask task) {
    long seniorCleanerCount =
        task.getAvailableCleaners().stream()
            .filter(cleaner -> cleaner.getType() == CleanerType.SENIOR)
            .count();
    if (seniorCleanerCount != 1) {
      throw new InvalidTaskException("The task must contain exactly one Senior cleaner type");
    }
    long juniorCleanerCount =
        task.getAvailableCleaners().stream()
            .filter(cleaner -> cleaner.getType() == CleanerType.JUNIOR)
            .count();
    if (juniorCleanerCount != 1) {
      throw new InvalidTaskException("The task must contain a Senior cleaner");
    }
  }
}
