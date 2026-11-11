package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa uma restrição de um problema de Simplex.
 * <p>
 * Restrições são limitações impostas ao problema. Cada restrição define
 * uma regra que a combinação das variáveis deve respeitar para que a solução
 * seja considerada viável.
 * <p>
 *
 * @see ConstraintType
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Constraint {
    /**
     * Variáveis da restrição.
     * <p>
     * O tamanho do array define o número de variáveis do problema.
     * Ex. {@code [2, -4, 1]} representa {@code 2x₁ - 4x₂ + x₃}.
     * </p>
     */
    private double[] coefficients;

    /**
     * Tipo de restrição representados pelo sinal de igualdade (&#8804, &#8805 ou
     * =).
     */
    private ConstraintType constraintType;

    /**
     * Valor constante do lado direito do sinal de igualdade.
     */
    private double rightHandValue;
}
