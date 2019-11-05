package com.newco.workforceoptimizer.web;

import com.newco.workforceoptimizer.dto.CleaningRequest;
import com.newco.workforceoptimizer.dto.CleaningResponse;
import com.newco.workforceoptimizer.model.cleaner.Cleaner;
import com.newco.workforceoptimizer.model.cleaner.CleanerType;
import com.newco.workforceoptimizer.model.task.CleaningTask;
import com.newco.workforceoptimizer.service.CleaningCapacityOptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/workforce/cleaning/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CleaningWorkforceController {

  private final CleaningCapacityOptimizationService capacityOptimizationService;

  @PostMapping(path = "/optimize/capacity")
  public List<CleaningResponse> optimizeCapacity(@RequestBody @Valid CleaningRequest request) {
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, request.getSenior());
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, request.getJunior());

    return request.getRooms().stream()
        .map(room -> new CleaningTask(room, Arrays.asList(seniorCleaner, juniorCleaner)))
        .map(capacityOptimizationService::optimize)
        .map(result -> new CleaningResponse(result.capacityOf(seniorCleaner), result.capacityOf(juniorCleaner)))
        .collect(Collectors.toList());
  }
}
