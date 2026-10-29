package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.solver.impl.SimplexSolver;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representa uma iteração do Tableau durante cálculo do simplex.
 *
 * @see SimplexSolver
 * @see Tableau
 */
@AllArgsConstructor
@Data
public class Iteration {
    /**
     * Título da Iteração. Pode ser {@code Inicial}, {@code Iteração} ou
     * {@code Final}.
     */
    String title;

    /**
     * Índices da coluna pivô e linha pivô do Tableau.
     */
    int[] pivot;

    /**
     * Nomes das linhas do Tableau.
     */
    String[] rowNames;

    /**
     * Nomes das colunas do Tableau.
     */
    String[] columnNames;

    /**
     * Matriz dos valores das variáveis do Tableau.
     */
    String[][] matrix;
}
