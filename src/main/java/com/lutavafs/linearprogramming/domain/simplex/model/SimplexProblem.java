package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.domain.simplex.enums.OptimizationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimplexProblem {
    private OptimizationType optimizationType;
    private double[] objectiveFunction;
    private List<Constraint> constraints;
}
