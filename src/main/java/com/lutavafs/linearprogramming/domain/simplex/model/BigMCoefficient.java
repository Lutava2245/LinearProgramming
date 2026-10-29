package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import com.lutavafs.linearprogramming.util.MathFormater;
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
        if (Math.abs(this.bigM - coefficient.bigM) > 1e-9) {
            return Double.compare(this.bigM, coefficient.bigM);
        }
        return Double.compare(this.value, coefficient.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BigMCoefficient coefficient = (BigMCoefficient) o;
        return Math.abs(this.bigM - coefficient.bigM) < 1e-9 && Math.abs(this.value - coefficient.value) < 1e-9;
    }

    @Override
    public int hashCode() {
        double valArredondado = Math.round(this.value / 1e-9) * 1e-9;
        double mArredondado = Math.round(this.bigM / 1e-9) * 1e-9;
        return Objects.hash(valArredondado, mArredondado);
    }

    @Override
    public @Nonnull String toString() {
        String valueString = MathFormater.formatDouble(value);
        String bigMString = MathFormater.formatDouble(bigM);

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