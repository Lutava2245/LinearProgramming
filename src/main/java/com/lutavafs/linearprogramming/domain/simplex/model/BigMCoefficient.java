package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import com.lutavafs.linearprogramming.util.MathHelper;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * Representa um coeficiente Big M utilizado nos cálculos do Simplex.
 * <p>
 * O método do Big M é uma técnica matemática usada em problemas de Simplex
 * onde há restrições do tipo {@link ConstraintType#GREATER_EQUAL} ou
 * {@link ConstraintType#EQUAL}.
 * <p>
 * Essas restrições exigem a adição de variáveis artificiais representadas
 * por um valor alto fictício de {@code M} como penalidade, fazendo o
 * algoritmo tentar zerar e expulsar as artificiais e garantindo a
 * viabilidade matemática do cálculo.
 * <p>
 *
 * @param value valor numérico real do coeficiente do Tableau.
 * @param bigM  número de valores artificiais {@code M} do coeficiente do
 *              Tableau.
 * @see Tableau
 * @see Constraint
 */
public record BigMCoefficient(double value, double bigM) {

    /**
     * Subtrai o valor real e os artificiais do coeficiente atual pelo coeficiente
     * recebido.
     *
     * @param coefficient coeficiente que subtrairá o coeficiente atual.
     * @return novo objeto {@link BigMCoefficient} contendo novo valor subtraído.
     */
    public BigMCoefficient subtract(BigMCoefficient coefficient) {
        return new BigMCoefficient(this.value - coefficient.value(), this.bigM - coefficient.bigM());
    }

    /**
     * Multiplica o valor real e os artificiais do coeficiente pelo valor recebido.
     *
     * @param num número multiplicador.
     * @return novo objeto {@link BigMCoefficient} contendo novo valor multiplicado.
     */
    public BigMCoefficient multiply(double num) {
        return new BigMCoefficient(this.value * num, this.bigM * num);
    }

    /**
     * Compara o valor real e os artificiais do coeficiente atual pelo valor real e
     * os artificiais do coeficiente recebido.
     *
     * @param coefficient coeficiente a comparar.
     * @return {@code 0} se o atual possui valor igual ao recebido;
     *         {@code -1} se o valor do atual for menor que o valor do recebido;
     *         {@code 1} se o valor do atual for maior que o valor do recebido.
     */
    public int compareTo(BigMCoefficient coefficient) {
        if (Math.abs(this.bigM - coefficient.bigM) > MathHelper.EPSILON) {
            return Double.compare(this.bigM, coefficient.bigM);
        }
        return Double.compare(this.value, coefficient.value);
    }

    /**
     * Verifica se o coeficiente é estritamente negativo, ou seja, se o valor real
     * e os artificiais do coeficiente são menores que zero.
     *
     * @return {@code true} se o coeficiente for estritamente negativo;
     *         {@code false} caso contrário.
     * @see MathHelper#EPSILON
     */
    public boolean isStrictlyNegative() {
        if (bigM < -MathHelper.EPSILON)
            return true;
        return Math.abs(bigM) <= MathHelper.EPSILON && value < -MathHelper.EPSILON;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BigMCoefficient coefficient = (BigMCoefficient) o;
        return Math.abs(this.bigM - coefficient.bigM) < MathHelper.EPSILON
                && Math.abs(this.value - coefficient.value) < MathHelper.EPSILON;
    }

    @Override
    public int hashCode() {
        double roundedValue = Math.round(this.value / MathHelper.EPSILON) * MathHelper.EPSILON;
        double roundedBigM = Math.round(this.bigM / MathHelper.EPSILON) * MathHelper.EPSILON;
        return Objects.hash(roundedValue, roundedBigM);
    }

    @Override
    public @Nonnull String toString() {
        String valueString = MathHelper.formatDouble(value);
        String bigMString = MathHelper.formatDouble(bigM);

        if (valueString.equals("0") && bigMString.equals("0")) {
            return "0";
        }

        if (!valueString.equals("0") && bigMString.equals("0")) {
            return valueString;
        }

        if (valueString.equals("0")) {
            if (bigMString.equals("1")) {
                return "M";
            } else if (bigMString.equals("-1")) {
                return "-M";
            }
            return bigMString + "M";
        }

        StringBuilder coefficient = new StringBuilder();

        coefficient.append(valueString);
        if (this.bigM > 0) {
            coefficient.append("+");
        }

        if (bigMString.equals("1")) {
            coefficient.append("M");
        } else if (bigMString.equals("-1")) {
            coefficient.append("-M");
        } else {
            coefficient.append(bigMString).append("M");
        }

        return coefficient.toString();
    }
}