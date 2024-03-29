package com.newco.workforceoptimizer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CleaningRequest {
  private List<@Min(value = 1, message = "The room count must be > 0") Integer> rooms;

  @Min(value = 1, message = "The senior capacity must be > 0")
  private Integer senior;

  @Min(value = 1, message = "The junior capacity must be > 0")
  private Integer junior;
}
