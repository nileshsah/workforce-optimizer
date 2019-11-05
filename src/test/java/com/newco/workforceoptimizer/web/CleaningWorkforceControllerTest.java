package com.newco.workforceoptimizer.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.newco.workforceoptimizer.dto.CleaningRequest;
import com.newco.workforceoptimizer.model.cleaner.Cleaner;
import com.newco.workforceoptimizer.model.cleaner.CleanerType;
import com.newco.workforceoptimizer.model.task.CleaningTaskResult;
import com.newco.workforceoptimizer.service.CleaningCapacityOptimizationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CleaningWorkforceControllerTest {

  private static final String OPTIMIZE_CAPACITY_ENDPOINT = "/workforce/cleaning/optimize/capacity";

  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired private MockMvc mockMvc;

  @MockBean private CleaningCapacityOptimizationService capacityOptimizationService;

  @Test
  public void shouldReturnSuccessForValidRequest() throws Exception {
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, 10);
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, 10);
    CleaningRequest request =
        CleaningRequest.builder()
            .rooms(Arrays.asList(15, 41, 67))
            .senior(seniorCleaner.getCleaningCapacity())
            .junior(juniorCleaner.getCleaningCapacity())
            .build();

    Mockito.when(capacityOptimizationService.optimize(Mockito.any()))
        .thenReturn(new CleaningTaskResult(ImmutableMap.of(seniorCleaner, 5, juniorCleaner, 3)));

    mockMvc
        .perform(
            post(OPTIMIZE_CAPACITY_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
        .andExpect(status().is2xxSuccessful());

    Mockito.verify(capacityOptimizationService, Mockito.times(3)).optimize(Mockito.any());
  }

  @Test
  public void shouldReturnBadRequestOnInvalidRequest() throws Exception {
    Cleaner seniorCleaner = new Cleaner(CleanerType.SENIOR, -1);
    Cleaner juniorCleaner = new Cleaner(CleanerType.JUNIOR, 10);
    CleaningRequest request =
        CleaningRequest.builder()
            .rooms(Arrays.asList(15, 41, -5))
            .senior(seniorCleaner.getCleaningCapacity())
            .junior(juniorCleaner.getCleaningCapacity())
            .build();

    mockMvc
        .perform(
            post(OPTIMIZE_CAPACITY_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    Mockito.verifyNoInteractions(capacityOptimizationService);
  }
}
