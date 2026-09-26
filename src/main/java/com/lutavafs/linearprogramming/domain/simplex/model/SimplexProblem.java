package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.domain.simplex.enums.OptimizationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class SimplexProblem {
    @NonNull private OptimizationType optimizationType;
    @NonNull private Double[] objectiveFunction;
    @NonNull private List<Constraint> constraints;
    private Tableau tableau = null;
}
