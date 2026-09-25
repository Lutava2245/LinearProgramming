package com.lutavafs.linearprogramming.domain.simplex.model;

public record Tableau(BigMCoefficient[] rowZ, double[][] matrix, String[] columnNames,
                      int[] basicIndexes) {

}
