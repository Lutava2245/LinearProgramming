package com.lutavafs.linearprogramming.domain.simplex.model;

import java.util.List;

import com.lutavafs.linearprogramming.solver.impl.SimplexSolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Representa a estrutura de matriz que organiza os dados do problema
 * de Simplex.
 * <p>
 * As linhas representam a função objetivo e as restrições do problema;
 * as colunas representam as variáveis básicas {@code x}, não-básicas
 * {@code s} e artificiais {@code a}.
 * <p>
 * A primeira coluna do Tableau é a variávei que representa o objetivo
 * {@code Z} ou {@code -Z}; a última coluna representa as constantes do
 * lado direito (Right Hand-Side).
 */
@RequiredArgsConstructor
@Getter
@Setter
public class Tableau {
    /**
     * Representa a primeira linha do Tableau. Este array contém os
     * valores das variáveis na Função Objetivo {@code Z} ou {@code -Z}.
     */
    private final BigMCoefficient[] rowZ;

    /**
     * Contém os valores das variáveis nas restrições.
     */
    private final double[][] matrix;

    /**
     * Contém os nomes de cada coluna do Tableau.
     */
    private final String[] columnNames;

    /**
     * Contém os índices das variáveis básicas do Tableau.
     */
    private final int[] basicIndexes;

    /**
     * Representa a coluna pivô utilizada no Simplex.
     */
    private int pivotColumn = -1;

    /**
     * Representa a linha pivô utilizada no Simplex.
     */
    private int pivotRow = -1;

    /**
     * Verifica se existem variáveis artificiais {@code a} no problema.
     *
     * @return {@code true} se tiver variáveis artificias;
     *         {@code false} caso contrário.
     * @see SimplexSolver
     */
    public boolean haveArtificialVariables() {
        for (String column : columnNames) {
            if (column.startsWith("a")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se uma coluna é básica no tableau.
     * <p>
     * Uma coluna é considerada básica se ela corresponde às variáveis que estão
     * atualmente na solução básica, ou seja, aquelas que têm um valor diferente de
     * zero na solução atual.
     * </p>
     * 
     * @param index índice da coluna a ser verificada.
     * @return {@code true} se a coluna for básica; {@code false} caso contrário.
     * @see SimplexSolver
     */
    public boolean isBasic(int index) {
        for (int basicIdx : this.getBasicIndexes()) {
            if (basicIdx == index)
                return true;
        }
        return false;
    }

    /**
     * Realiza o pivoteamento do Tableau.
     * <p>
     * Após a identificação do pivô, este método movimenta as variáveis,
     * trocando a variável da linha pivô pela variável da coluna pivô. A
     * intersecção entre a linha e coluna é o elemento pivô, ele é usado
     * para realizar as operações nos valores durante a movimentação.
     * <p>
     * O primeiro movimento é a atualização da linha pivô, onde todos os
     * valores da linha são divididos pelo valor do elemento pivô.
     * O segundo movimento é a atualização dos valores das outras linhas,
     * utilizando a seguinte fórmula:
     * {@code novo valor = valor atual - valor da coluna pivô na linha atual * elemento pivô}.
     * <p>
     *
     * @see SimplexSolver#iterate(SimplexProblem, List)
     */
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
