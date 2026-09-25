package com.lutavafs.linearprogramming.util;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import com.lutavafs.linearprogramming.domain.simplex.enums.OptimizationType;
import com.lutavafs.linearprogramming.domain.simplex.model.Constraint;
import com.lutavafs.linearprogramming.solver.SimplexSolver;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SimplexTestFactories {

    public static @NonNull SimplexSolver getMaxExample() {
        SimplexSolver solver = new SimplexSolver();
        solver.setOptimizationType(OptimizationType.MAXIMIZATION);

        solver.setObjectiveFunction(new double[] {5.0, 4.0, 3.0, 6.0});

        solver.setConstraints(
                List.of(
                        new Constraint(new double[]{1.0, 1.0, 1.0, 1.0}, ConstraintType.LESS_EQUAL, 20.0),
                        new Constraint(new double[]{2.0, 1.0, 3.0, 4.0}, ConstraintType.GREATER_EQUAL, 24.0),
                        new Constraint(new double[]{1.0, 2.0, 0.0, 1.0}, ConstraintType.EQUAL, 15.0)
                )
        );
        return solver;
    }

    public static @NonNull SimplexSolver getMinExample() {
        SimplexSolver solver = new SimplexSolver();
        solver.setOptimizationType(OptimizationType.MINIMIZATION);

        solver.setObjectiveFunction(new double[] {3.0, 2.0, 5.0, 4.0});

        solver.setConstraints(
                List.of(
                        new Constraint(new double[]{2.0, 1.0, 3.0, 1.0}, ConstraintType.LESS_EQUAL, 40.0),
                        new Constraint(new double[]{1.0, 2.0, 1.0, 2.0}, ConstraintType.GREATER_EQUAL, 20.0),
                        new Constraint(new double[]{3.0, 0.0, 2.0, 2.0}, ConstraintType.EQUAL, 30.0)
                )
        );
        return solver;
    }
}
