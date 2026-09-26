package com.lutavafs.linearprogramming.solver.impl;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import com.lutavafs.linearprogramming.domain.simplex.enums.OptimizationType;
import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;
import com.lutavafs.linearprogramming.domain.simplex.model.*;
import com.lutavafs.linearprogramming.solver.Solver;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.lutavafs.linearprogramming.util.MathFormater.formatDouble;

@Service
public class SimplexSolver implements Solver<SimplexProblem, SimplexResult> {

    @Override
    public SimplexResult calculate(SimplexProblem problem) {
        List<Iteration> iterations = new ArrayList<>();

        createTableau(problem);
        iterations.add(getIteration(problem, "Inicial"));

        if (problem.getTableau().haveArtificialVariables())
            resetZRow(problem.getTableau());

        iterate(problem, iterations);

        iterations.add(getIteration(problem, "Final"));
        return getResult(problem, iterations);
    }

    public void createTableau(SimplexProblem problem) {
        int numX = problem.getObjectiveFunction().length;
        int numS = 0;
        int numA = 0;

        for (Constraint c : problem.getConstraints()) {
            switch (c.getConstraintType()) {
                case LESS_EQUAL:
                    numS++;
                    break;

                case GREATER_EQUAL:
                    numS++;
                    numA++;
                    break;

                default:
                    numA++;
                    break;
            }
        }

        int totalColumns = 1 + numX + numS + numA + 1;
        int totalRows = problem.getConstraints().size();

        int[] basicIndexes = new int[totalRows];
        double[][] matrix = new double[totalRows][totalColumns];
        BigMCoefficient[] rowZ = new BigMCoefficient[totalColumns];
        String[] columnNames = new String[totalColumns];

        columnNames[0] = problem.getOptimizationType() == OptimizationType.MAXIMIZATION ? "Z" : "-Z";
        int colIdx = 1;
        for (int i = 1; i <= numX; i++) {
            columnNames[colIdx++] = "x" + i;
        }
        for (int i = 1; i <= numS; i++) {
            columnNames[colIdx++] = "s" + i;
        }
        for (int i = 1; i <= numA; i++) {
            columnNames[colIdx++] = "a" + i;
        }
        columnNames[totalColumns - 1] = "RHS";

        rowZ[0] = new BigMCoefficient(1, 0);
        for (int i = 0; i < numX; i++) {
            rowZ[1 + i] = new BigMCoefficient(problem.getOptimizationType() == OptimizationType.MAXIMIZATION ? -problem.getObjectiveFunction()[i] : problem.getObjectiveFunction()[i], 0);
        }
        for (int i = 0; i < numS; i++) {
            rowZ[1 + numX + i] = new BigMCoefficient(0, 0);
        }
        for (int i = 0; i < numA; i++) {
            rowZ[1 + numX + numS + i] = new BigMCoefficient(0, 1.0);
        }
        rowZ[totalColumns - 1] = new BigMCoefficient(0, 0);

        int sIdx = 1 + numX;
        int aIdx = 1 + numX + numS;

        for (int i = 0; i < problem.getConstraints().size(); i++) {
            Constraint c = problem.getConstraints().get(i);

            matrix[i][0] = 0.0;

            System.arraycopy(c.getCoefficients(), 0, matrix[i], 1, numX);

            if (c.getConstraintType() == ConstraintType.LESS_EQUAL) {
                matrix[i][sIdx] = 1.0;
                basicIndexes[i] = sIdx;
                sIdx++;
            } else if (c.getConstraintType() == ConstraintType.GREATER_EQUAL) {
                matrix[i][sIdx] = -1.0;
                matrix[i][aIdx] = 1.0;
                basicIndexes[i] = aIdx;
                sIdx++;
                aIdx++;
            } else if (c.getConstraintType() == ConstraintType.EQUAL) {
                matrix[i][aIdx] = 1.0;
                basicIndexes[i] = aIdx;
                aIdx++;
            }

            matrix[i][totalColumns - 1] = c.getRightHandValue();
        }

        problem.setTableau(new Tableau(
                rowZ,
                matrix,
                columnNames,
                basicIndexes
        ));
    }

    protected void resetZRow(Tableau tableau) {
        for (int i = 0; i < tableau.getBasicIndexes().length; i++) {
            if (tableau.getColumnNames()[tableau.getBasicIndexes()[i]].startsWith("a")) {
                for (int j = 0; j < tableau.getRowZ().length; j++) {
                    double valorRestricao = tableau.getMatrix()[i][j];
                    tableau.getRowZ()[j] = tableau.getRowZ()[j].subtract(new BigMCoefficient(0.0, valorRestricao));
                }
            }
        }
    }

    public void iterate(SimplexProblem problem, List<Iteration> iterations) {
        do {
            BigMCoefficient lowestCoefficient = findPivotColumn(problem.getTableau());
            if (lowestCoefficient.bigM() >= 0 && lowestCoefficient.value() >= 0)
                break;

            findPivotRow(problem.getTableau());
            iterations.add(getIteration(problem, "Iteração"));
            problem.getTableau().pivot();
        } while (true);
    }

    public BigMCoefficient findPivotColumn(Tableau tableau) {
        BigMCoefficient lowestCoefficient = new BigMCoefficient(Double.MAX_VALUE, Double.MAX_VALUE);
        for (int i = 1; i < tableau.getRowZ().length - 1; i++) {
            BigMCoefficient coefficient = tableau.getRowZ()[i];
            if (lowestCoefficient.compareTo(coefficient) > 0) {
                lowestCoefficient = coefficient;
                tableau.setPivotColumn(i);
            }
        }

        return lowestCoefficient;
    }

    public void findPivotRow(Tableau tableau) {
        double lowest = Double.MAX_VALUE;
        for (int i = 0; i < tableau.getMatrix().length; i++) {
            double div = tableau.getMatrix()[i][tableau.getMatrix()[i].length - 1]
                    / tableau.getMatrix()[i][tableau.getPivotColumn()];
            if (div < 0)
                continue;
            if (lowest > div) {
                lowest = div;
                tableau.setPivotRow(i);
            }
        }
    }

    protected Iteration getIteration(SimplexProblem problem, String title) {
        Tableau tableau = problem.getTableau();

        String[] rowNames = new String[tableau.getMatrix().length + 1];
        String[][] matrix = new String[tableau.getMatrix().length + 1][tableau.getMatrix()[0].length];

        for (int i = 0; i < tableau.getRowZ().length; i++) {
            matrix[0][i] = tableau.getRowZ()[i].toString();
        }

        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i + 1][j] = (formatDouble(tableau.getMatrix()[i][j]));
            }
        }

        rowNames[0] = tableau.getColumnNames()[0];
        for (int i = 0; i < problem.getConstraints().size(); i++) {
            rowNames[i + 1] = tableau.getColumnNames()[tableau.getBasicIndexes()[i]];
        }

        return new Iteration(
                title,
                title.equals("Final") ? new int[] {-1, -1} : new int[] {tableau.getPivotColumn(), tableau.getPivotRow()},
                rowNames,
                tableau.getColumnNames(),
                matrix
        );
    }

    public SimplexResult getResult(SimplexProblem problem, List<Iteration> iterations) {
        Tableau tableau = problem.getTableau();

        final int rightHandColumn = tableau.getColumnNames().length - 1;
        Map<String, String> variableValues = getVariableValues(tableau);

        for (int i = 0; i < tableau.getBasicIndexes().length; i++) {
            String variableName = tableau.getColumnNames()[tableau.getBasicIndexes()[i]];

            if (variableName.startsWith("a") && tableau.getMatrix()[i][rightHandColumn] > 1e-6) {
                return new SimplexResult(iterations, StatusResult.INFEASIBLE.getTitle(), problem.getOptimizationType().getTitle(), formatDouble(0), variableValues);
            }
        }

        boolean infinity = false;
        for (int j = 1; j < rightHandColumn; j++) {
            if (tableau.getColumnNames()[j].startsWith("x") && !isBasic(tableau, j)) {
                BigMCoefficient coefficient = tableau.getRowZ()[j];
                if (Math.abs(coefficient.bigM()) < 1e-6 && Math.abs(coefficient.value()) < 1e-6) {
                    infinity = true;
                    break;
                }
            }
        }

        double objectiveValue = problem.getOptimizationType() == OptimizationType.MAXIMIZATION
                ? tableau.getRowZ()[rightHandColumn].value()
                : tableau.getRowZ()[rightHandColumn].value() * (-1);

        StatusResult finalStatus = infinity
                ? StatusResult.FEASIBLE
                : StatusResult.OPTIMAL;

        return new SimplexResult(iterations, finalStatus.getTitle(), problem.getOptimizationType().getTitle(), formatDouble(objectiveValue), variableValues);
    }

    private @NonNull Map<String, String> getVariableValues(Tableau tableau) {
        Map<String, String> variableValues = new TreeMap<>();

        for (int j = 1; j < tableau.getColumnNames().length - 1; j++) {
            variableValues.put(tableau.getColumnNames()[j], "0");
        }

        for (int i = 0; i < tableau.getBasicIndexes().length; i++) {
            int basicColumn = tableau.getBasicIndexes()[i];
            variableValues.put(tableau.getColumnNames()[basicColumn], formatDouble(tableau.getMatrix()[i][tableau.getColumnNames().length - 1]));
        }

        Map<String, String> sortedVariables = new LinkedHashMap<>();

        variableValues.forEach((key, value) -> {
            if (key.startsWith("x")) sortedVariables.put(key, value);
        });
        variableValues.forEach((key, value) -> {
            if (key.startsWith("s")) sortedVariables.put(key, value);
        });
        variableValues.forEach((key, value) -> {
            if (key.startsWith("a")) sortedVariables.put(key, value);
        });

        return sortedVariables;
    }

    private boolean isBasic(Tableau tableau, int index) {
        for (int basicIdx : tableau.getBasicIndexes()) {
            if (basicIdx == index) return true;
        }
        return false;
    }
}
