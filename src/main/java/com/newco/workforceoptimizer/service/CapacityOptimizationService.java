package com.newco.workforceoptimizer.service;

public interface CapacityOptimizationService<T, S> {
  S optimize(T task);
}
