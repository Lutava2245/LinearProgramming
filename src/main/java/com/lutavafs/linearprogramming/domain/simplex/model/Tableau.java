package com.lutavafs.linearprogramming.domain.simplex.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Tableau {
    private final BigMCoefficient[] rowZ;
    private final double[][] matrix;
    private final String[] columnNames;
    private final int[] basicIndexes;
    private int pivotColumn = -1;
    private int pivotRow = -1;

    public boolean haveArtificialVariables() {
        for (String column : columnNames) {
            if (column.startsWith("a")) {
                return true;
            }
        }
        return false;
    }

    public void pivot() {
        basicIndexes[pivotRow] = pivotColumn;

        double pivotElement = matrix[pivotRow][pivotColumn];

        for (int i = 0; i < matrix[pivotRow].length; i++) {
            matrix[pivotRow][i] /= pivotElement;
        }

        for (int i = 0; i < rowZ.length; i++) {
            if (i == pivotColumn)
                continue;
            rowZ[i] = rowZ[i].subtract(rowZ[pivotColumn].multiply(matrix[pivotRow][i]));
        }
        rowZ[pivotColumn] = rowZ[pivotColumn].subtract(rowZ[pivotColumn].multiply(matrix[pivotRow][pivotColumn]));

        for (int i = 0; i < matrix.length; i++) {
            if (i == pivotRow)
                continue;
            for (int j = 0; j < matrix[i].length; j++) {
                if (j == pivotColumn)
                    continue;
                matrix[i][j] -= matrix[i][pivotColumn] * matrix[pivotRow][j];
            }
            matrix[i][pivotColumn] -= matrix[i][pivotColumn] * matrix[pivotRow][pivotColumn];
        }
    }
}
