package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;
import com.lutavafs.linearprogramming.solver.impl.SimplexSolver;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Representa o resultado final do cálculo de Simplex.
 *
 * @see SimplexSolver
 */
@AllArgsConstructor
@Data
public class SimplexResult {
    /**
     * Iterações do Tableau feitas durante o processo de Simplex.
     */
    private List<Iteration> iterations;

    /**
     * Status do resultado do Simplex.
     *
     * @see StatusResult
     */
    private String status;

    /**
     * Tipo de otimização do problema de Simplex.
     */
    private String optimizationType;

    /**
     * Valor final da solução objetivo.
     */
    private String objectiveValue;

    /**
     * Nomes das variáveis do Simplex e seus valores finais.
     */
    private Map<String, String> variableValues;
}
