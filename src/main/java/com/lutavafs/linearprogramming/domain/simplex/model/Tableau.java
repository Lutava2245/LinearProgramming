package com.lutavafs.linearprogramming.domain.simplex.model;

import java.util.List;

import com.lutavafs.linearprogramming.solver.impl.SimplexSolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Representa a estrutura de matriz que organiza os dados do problema de
 * Programação Linear no Método Simplex.
 * <p>
 * As linhas representam a função objetivo e as restrições do problema.
 * As colunas são divididas em:
 * <ul>
 * <li>{@code Z} ou {@code -Z}: Coluna de controle da função objetivo.</li>
 * <li>{@code x}: Variáveis de decisão.</li>
 * <li>{@code s}: Variáveis de folga ou excesso.</li>
 * <li>{@code a}: Variáveis artificiais.</li>
 * <li>{@code RHS}: Coluna do lado direito (Right Hand-Side) com as constantes
 * das restrições.</li>
 * </ul>
 * <p>
 * O estado de cada variável (se pertence à base ou não) muda dinamicamente a
 * cada pivoteamento.
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
     * Verifica se uma coluna do Tableau pertence à base atual.
     * <p>
     * Uma coluna é considerada básica se ela representa uma variável que está
     * atualmente na base (formando um vetor unitário na matriz).
     * <p>
     * <b>Nota:</b> Variáveis básicas podem assumir valor zero na solução (estado de
     * degeneração). Por definição, apenas as variáveis não-básicas possuem valor
     * fixo em zero.
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
     * Realiza a operação de pivoteamento no Tableau (Eliminação de Gauss-Jordan).
     * <p>
     * O processo ocorre em duas etapas principais:
     * <ol>
     * <li><b>Atualização da Linha Pivô:</b> Todos os elementos da linha pivô são
     * divididos pelo elemento pivô, normalizando-o para 1.</li>
     * <li><b>Atualização das Demais Linhas (incluindo a linha Z):</b> Para cada
     * outra linha {@code i}, aplica-se a operação de eliminação:
     * <br>
     * {@code Nova Linha_i = Linha_i - (Coeficiente na coluna pivô) * (Linha Pivô Atualizada)}.
     * </li>
     * </ol>
     *
     * @see SimplexSolver
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
