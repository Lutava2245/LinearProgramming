package com.lutavafs.linearprogramming.domain.simplex.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Iteration {
    String title;
    int[] pivot;
    String[] rowNames;
    String[] columnNames;
    String[][] matrix;
}
