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
     * Coeficientes numéricos das variáveis na restrição.
     * <p>
     * O tamanho do array corresponde ao número de variáveis de decisão do problema
     * (definido pela função objetivo), sendo que cada elemento representa o peso
     * da variável correspondente na restrição.
     * <br>
     * Ex: {@code [2.0, -4.0, 1.0]} representa {@code 2x₁ - 4x₂ + x₃}.
     */
    private double[] coefficients;

    /**
     * Tipo de restrição representados pelo sinal de igualdade (&#8804;;, &#8805; ou
     * =).
     */
    private ConstraintType constraintType;

    /**
     * Valor constante do lado direito do sinal de igualdade.
     */
    private double rightHandValue;
}
