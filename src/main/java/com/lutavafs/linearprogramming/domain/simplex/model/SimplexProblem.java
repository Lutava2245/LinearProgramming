package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.domain.simplex.enums.OptimizationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Representa um problema de programação linear que utiliza o método Simplex.
 * <p>
 * Um problema de Simplex é uma situação onde o objetivo é verificar se,
 * utilizando as variáveis dentro das limitações {@link Constraint},
 * é possível chegar no maior ou menor valor possível. O valor é representado
 * como {@code Z} para maximização ou {@code -Z} para minimização.
 * <p>
 *
 * @see OptimizationType
 * @see Constraint
 */
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class SimplexProblem {
    /**
     * Tipo de otimização do problema.
     */
    @NonNull
    private OptimizationType optimizationType;

    /**
     * Variáveis da Função Objetivo.
     * <p>
     * A Função Objetivo é uma equação matemática que expressa o valor máximo
     * ou mínimo pelas variáveis de decisão.
     * Ex. {@code Max Z = 5x₁ + 4x₂ + 3x₃})
     * <p>
     */
    @NonNull
    private Double[] objectiveFunction;

    /**
     * Restrições do problema.
     */
    @NonNull
    private List<Constraint> constraints;

    /**
     * Tableau pré-definido como {@code null} para ser
     * instanciado e utilizado nos cálculos do Simplex.
     */
    private Tableau tableau = null;
}
