package com.lutavafs.linearprogramming.domain.simplex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimplexResult {
    private List<Iteration> iterations;
    private String status;
    private String optimizationType;
    private double objectiveValue;
    private Map<String, String> variableValues;
}
