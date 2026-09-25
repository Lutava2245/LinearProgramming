package com.lutavafs.linearprogramming.service.impl;

import com.lutavafs.linearprogramming.domain.simplex.model.SimplexProblem;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.service.SimplexService;
import com.lutavafs.linearprogramming.solver.SimplexSolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SimplexServiceImpl implements SimplexService {

    private final SimplexSolver simplexSolver;

    public SimplexResult calculate(SimplexProblem problem) {
        simplexSolver.setConstraints(problem.getConstraints());
        simplexSolver.setObjectiveFunction(problem.getObjectiveFunction());
        simplexSolver.setOptimizationType(problem.getOptimizationType());

        return simplexSolver.calculate();
    }
}
