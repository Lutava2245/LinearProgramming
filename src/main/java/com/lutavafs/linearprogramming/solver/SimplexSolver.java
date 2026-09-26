package com.lutavafs.linearprogramming.solver;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import com.lutavafs.linearprogramming.domain.simplex.enums.OptimizationType;
import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;
import com.lutavafs.linearprogramming.domain.simplex.model.*;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.lutavafs.linearprogramming.util.MathFormater.formatDouble;

@Getter
@Setter
@Service
public class SimplexSolver {
    private OptimizationType optimizationType;
    private double[] objectiveFunction;
    private List<Constraint> constraints;
    private Tableau tableau;
    private int pivotColumn = -1;
    private int pivotRow = -1;
    private List<Iteration> iterations = new ArrayList<>();

    public SimplexResult calculate() {
        createTableau();
        iterations.add(getIteration("Inicial"));

        if (tableau.haveArtificialVariables())
            resetZRow();

        iterate();

        iterations.add(getIteration("Final"));
        return getResult();
    }

    public void createTableau() {
        pivotColumn = -1;
        pivotRow = -1;
        iterations = new ArrayList<>();

        int numX = objectiveFunction.length;
        int numS = 0;
        int numA = 0;

        for (Constraint c : constraints) {
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
        int totalRows = constraints.size();

        int[] basicIndexes = new int[totalRows];
        double[][] matrix = new double[totalRows][totalColumns];
        BigMCoefficient[] rowZ = new BigMCoefficient[totalColumns];
        String[] columnNames = new String[totalColumns];

        columnNames[0] = optimizationType == OptimizationType.MAXIMIZATION ? "Z" : "-Z";
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
            rowZ[1 + i] = new BigMCoefficient(optimizationType == OptimizationType.MAXIMIZATION ? -objectiveFunction[i] : objectiveFunction[i], 0);
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

        for (int i = 0; i < constraints.size(); i++) {
            Constraint c = constraints.get(i);

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

        this.tableau = new Tableau(
                rowZ,
                matrix,
                columnNames,
                basicIndexes
        );
    }

    protected void resetZRow() {
        for (int i = 0; i < tableau.basicIndexes().length; i++) {
            if (tableau.columnNames()[tableau.basicIndexes()[i]].startsWith("a")) {
                for (int j = 0; j < tableau.rowZ().length; j++) {
                    double valorRestricao = tableau.matrix()[i][j];
                    tableau.rowZ()[j] = tableau.rowZ()[j].subtract(new BigMCoefficient(0.0, valorRestricao));
                }
            }
        }
    }

    public void iterate() {
        do {
            BigMCoefficient lowestCoefficient = findPivotColumn();
            if (lowestCoefficient.bigM() >= 0 && lowestCoefficient.value() >= 0)
                break;

            findPivotRow();
            iterations.add(getIteration("Iteração"));
            tableau.pivot(pivotRow, pivotColumn);
        } while (true);
    }

    public BigMCoefficient findPivotColumn() {
        BigMCoefficient lowestCoefficient = new BigMCoefficient(Double.MAX_VALUE, Double.MAX_VALUE);
        for (int i = 1; i < tableau.rowZ().length - 1; i++) {
            BigMCoefficient coefficient = tableau.rowZ()[i];
            if (lowestCoefficient.compareTo(coefficient) > 0) {
                lowestCoefficient = coefficient;
                pivotColumn = i;
            }
        }

        return lowestCoefficient;
    }

    public void findPivotRow() {
        double lowest = Double.MAX_VALUE;
        for (int i = 0; i < tableau.matrix().length; i++) {
            double div = tableau.matrix()[i][tableau.matrix()[i].length - 1]
                    / tableau.matrix()[i][pivotColumn];
            if (div < 0)
                continue;
            if (lowest > div) {
                lowest = div;
                pivotRow = i;
            }
        }
    }

    protected Iteration getIteration(String title) {
        String[] rowNames = new String[tableau.matrix().length + 1];
        String[][] matrix = new String[tableau.matrix().length + 1][tableau.matrix()[0].length];

        for (int i = 0; i < tableau.rowZ().length; i++) {
            matrix[0][i] = tableau.rowZ()[i].toString();
        }

        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i + 1][j] = (formatDouble(tableau.matrix()[i][j]));
            }
        }

        rowNames[0] = tableau.columnNames()[0];
        for (int i = 0; i < constraints.size(); i++) {
            rowNames[i + 1] = tableau.columnNames()[tableau.basicIndexes()[i]];
        }

        return new Iteration(
                title,
                title.equals("Final") ? new int[] {-1, -1} : new int[] {pivotColumn, pivotRow},
                rowNames,
                tableau.columnNames(),
                matrix
        );
    }

    public SimplexResult getResult() {
        final int rightHandColumn = tableau.columnNames().length - 1;
        Map<String, String> variableValues = getVariableValues();

        for (int i = 0; i < tableau.basicIndexes().length; i++) {
            String variableName = tableau.columnNames()[tableau.basicIndexes()[i]];

            if (variableName.startsWith("a") && tableau.matrix()[i][rightHandColumn] > 1e-6) {
                return new SimplexResult(iterations, StatusResult.INFEASIBLE.getTitle(), optimizationType.getTitle(), formatDouble(0), variableValues);
            }
        }

        boolean infinity = false;
        for (int j = 1; j < rightHandColumn; j++) {
            if (tableau.columnNames()[j].startsWith("x") && !isBasic(j)) {
                BigMCoefficient coefficient = tableau.rowZ()[j];
                if (Math.abs(coefficient.bigM()) < 1e-6 && Math.abs(coefficient.value()) < 1e-6) {
                    infinity = true;
                    break;
                }
            }
        }

        double objectiveValue = optimizationType == OptimizationType.MAXIMIZATION
                ? tableau.rowZ()[rightHandColumn].value()
                : tableau.rowZ()[rightHandColumn].value() * (-1);

        StatusResult finalStatus = infinity
                ? StatusResult.FEASIBLE
                : StatusResult.OPTIMAL;

        return new SimplexResult(iterations, finalStatus.getTitle(), optimizationType.getTitle(), formatDouble(objectiveValue), variableValues);
    }

    private @NonNull Map<String, String> getVariableValues() {
        Map<String, String> variableValues = new TreeMap<>();

        for (int j = 1; j < tableau.columnNames().length - 1; j++) {
            variableValues.put(tableau.columnNames()[j], "0");
        }

        for (int i = 0; i < tableau.basicIndexes().length; i++) {
            int basicColumn = tableau.basicIndexes()[i];
            variableValues.put(tableau.columnNames()[basicColumn], formatDouble(tableau.matrix()[i][tableau.columnNames().length - 1]));
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

    private boolean isBasic(int index) {
        for (int basicIdx : tableau.basicIndexes()) {
            if (basicIdx == index) return true;
        }
        return false;
    }
}
