package com.lutavafs.linearprogramming.service;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import com.lutavafs.linearprogramming.domain.simplex.enums.OptimizationType;
import com.lutavafs.linearprogramming.domain.simplex.model.Constraint;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.solver.*;

import java.util.List;
import java.util.Scanner;

public class Service {
    private static final Scanner scan = new Scanner(System.in);

    public static void selecionarAlgoritmo(String[] args) {
        System.out.println("""
                Escolha um algoritmo:
                1 - Simplex
                2 - Problema de Transporte
                """);
        if (scan.nextInt() == 1) {
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
            SimplexResult result = solver.calculate();
        } else {
            Transporte.calcular();
        }
    }
}