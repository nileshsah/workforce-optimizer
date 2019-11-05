package com.newco.workforceoptimizer.service;

import com.newco.workforceoptimizer.model.cleaner.Cleaner;
import com.newco.workforceoptimizer.model.cleaner.CleanerType;
import com.newco.workforceoptimizer.model.task.CleaningTask;
import com.newco.workforceoptimizer.model.task.CleaningTaskResult;
import com.newco.workforceoptimizer.service.exception.InvalidTaskException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CleaningCapacityOptimizationServiceTest {

  private CleaningCapacityOptimizationService capacityOptimizationService;

  @BeforeEach
  public void setup() {
    this.capacityOptimizationService = new CleaningCapacityOptimizationService();
  }

  @Test
  public void shouldCalculateCorrectOptimizedCapacityValueForValidTaskI() {
    // Given
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, 10);
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, 6);
    List<Cleaner> cleaners = Arrays.asList(seniorCleaner, juniorCleaner);

    CleaningTask task = new CleaningTask(35, cleaners);
    CleaningTask task2 = new CleaningTask(21, cleaners);
    CleaningTask task3 = new CleaningTask(17, cleaners);
    CleaningTask task4 = new CleaningTask(28, cleaners);

    // When
    CleaningTaskResult optimizedCount = capacityOptimizationService.optimize(task);

    // Then
    Assertions.assertEquals(3, optimizedCount.capacityOf(seniorCleaner));
    Assertions.assertEquals(1, optimizedCount.capacityOf(juniorCleaner));
  }

  @Test
  public void shouldCalculateCorrectOptimizedCapacityValueForValidTaskII() {
    // Given
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, 10);
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, 6);
    List<Cleaner> cleaners = Arrays.asList(seniorCleaner, juniorCleaner);

    CleaningTask task = new CleaningTask(21, cleaners);

    // When
    CleaningTaskResult optimizedCount = capacityOptimizationService.optimize(task);

    // Then
    Assertions.assertEquals(1, optimizedCount.capacityOf(seniorCleaner));
    Assertions.assertEquals(2, optimizedCount.capacityOf(juniorCleaner));
  }

  @Test
  public void shouldCalculateCorrectOptimizedCapacityValueForValidTaskIII() {
    // Given
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, 10);
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, 6);
    List<Cleaner> cleaners = Arrays.asList(seniorCleaner, juniorCleaner);

    CleaningTask task = new CleaningTask(17, cleaners);

    // When
    CleaningTaskResult optimizedCount = capacityOptimizationService.optimize(task);

    // Then
    Assertions.assertEquals(2, optimizedCount.capacityOf(seniorCleaner));
    Assertions.assertEquals(0, optimizedCount.capacityOf(juniorCleaner));
  }

  @Test
  public void shouldCalculateCorrectOptimizedCapacityValueForValidTaskIV() {
    // Given
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, 11);
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, 6);
    List<Cleaner> cleaners = Arrays.asList(seniorCleaner, juniorCleaner);

    CleaningTask task = new CleaningTask(24, cleaners);

    // When
    CleaningTaskResult optimizedCount = capacityOptimizationService.optimize(task);

    // Then
    Assertions.assertEquals(2, optimizedCount.capacityOf(seniorCleaner));
    Assertions.assertEquals(1, optimizedCount.capacityOf(juniorCleaner));
  }

  @Test
  public void shouldCalculateCorrectOptimizedCapacityValueForValidTaskV() {
    // Given
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, 10);
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, 6);
    List<Cleaner> cleaners = Arrays.asList(seniorCleaner, juniorCleaner);

    CleaningTask task = new CleaningTask(28, cleaners);

    // When
    CleaningTaskResult optimizedCount = capacityOptimizationService.optimize(task);

    // Then
    Assertions.assertEquals(1, optimizedCount.capacityOf(seniorCleaner));
    Assertions.assertEquals(3, optimizedCount.capacityOf(juniorCleaner));
  }

  @Test
  public void shouldThrowExceptionWhenCapacityIsNegative() {
    // Given
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, 11);
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, -6);
    List<Cleaner> cleaners = Arrays.asList(seniorCleaner, juniorCleaner);

    CleaningTask task = new CleaningTask(24, cleaners);

    // When and Then
    Assertions.assertThrows(InvalidTaskException.class, () -> capacityOptimizationService.optimize(task));
  }

  @Test
  public void shouldThrowExceptionWhenSeniorCleanerNotPresent() {
    // Given
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, 6);
    List<Cleaner> cleaners = Arrays.asList(juniorCleaner);

    CleaningTask task = new CleaningTask(24, cleaners);

    // When and Then
    Assertions.assertThrows(InvalidTaskException.class, () -> capacityOptimizationService.optimize(task));
  }

  @Test
  public void shouldThrowExceptionWhenJuniorCleanerNotPresent() {
    // Given
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, 6);
    List<Cleaner> cleaners = Arrays.asList(seniorCleaner);

    CleaningTask task = new CleaningTask(24, cleaners);

    // When and Then
    Assertions.assertThrows(InvalidTaskException.class, () -> capacityOptimizationService.optimize(task));
  }

}
