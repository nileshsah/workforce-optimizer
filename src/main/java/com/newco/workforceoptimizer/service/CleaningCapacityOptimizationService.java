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

  private static final Integer INFINITY = Integer.MAX_VALUE;

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

    final Map<Cleaner, Integer> optimizedCleanerCapacity =
        new HashMap<Cleaner, Integer>() {
          {
            put(seniorCleaner, INFINITY);
            put(juniorCleaner, INFINITY);
          }
        };

    final Integer maximumSeniorCapacity = 1 + (roomsToClean / seniorCleaner.getCleaningCapacity());

    for(Long seniorCleanerCount = 1L; seniorCleanerCount <= maximumSeniorCapacity; seniorCleanerCount++) {
      Long remainingRoomsToClean =
          Math.max(0, roomsToClean - (seniorCleanerCount * seniorCleaner.getCleaningCapacity()));
      Long juniorCleanerCount =
          (long) Math.ceil((double) remainingRoomsToClean / juniorCleaner.getCleaningCapacity());

      Long optimizedCapacity = optimizedCleanerCapacity.entrySet().stream()
          .mapToLong(kv -> (long) kv.getKey().getCleaningCapacity() * kv.getValue())
          .sum();
      Long currentCapacity = (seniorCleaner.getCleaningCapacity() * seniorCleanerCount)
          + (juniorCleaner.getCleaningCapacity() * juniorCleanerCount);

      if (currentCapacity < optimizedCapacity) {
        optimizedCleanerCapacity.put(seniorCleaner, seniorCleanerCount.intValue());
        optimizedCleanerCapacity.put(juniorCleaner, juniorCleanerCount.intValue());
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
    task.getAvailableCleaners().stream()
        .mapToInt(Cleaner::getCleaningCapacity)
        .min()
        .ifPresent(minCapacity -> {
          if(minCapacity <= 0) {
            throw new InvalidTaskException("The cleaner capacity must be > 0");
          }
        });
  }
}
