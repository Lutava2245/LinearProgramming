package com.lutavafs.linearprogramming.domain.simplex.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class SimplexResult {
    private List<Iteration> iterations;
    private String status;
    private String optimizationType;
    private String objectiveValue;
    private Map<String, String> variableValues;
}
