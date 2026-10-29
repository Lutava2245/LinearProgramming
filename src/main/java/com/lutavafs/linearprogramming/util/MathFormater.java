package com.lutavafs.linearprogramming.util;

/**
 * Utilitário para formatação e cálculos com dados numéricos.
 */
public class MathFormater {
    private static final double EPSILON = 1e-9;

    /**
     * Formata um número do tipo {@code double} para um {@code String} formatado.
     *
     * @param valor valor numérico a ser formatado.
     * @return um {@code String} formatado.
     */
    public static String formatDouble(double valor) {
        if (Double.isNaN(valor) || Double.isInfinite(valor))
            return String.valueOf(valor);

        if (Math.abs(valor) < 1e-9) {
            return "0";
        }

        for (int d = 1; d <= 10000; d++) {
            long n = Math.round(valor * d);

            if (Math.abs((double) n / d - valor) < EPSILON) {
                final long GCD = greatestCommonDivisor(n, d);
                long simplifiedNum = n / GCD;
                long simplifiedDen = d / GCD;

                return simplifiedDen == 1 ? String.valueOf(simplifiedNum) : simplifiedNum + "/" + simplifiedDen;
            }
        }

        return String.valueOf(valor);
    }

    /**
     * Calcula o Máximo Divisor Comum (MDC) de 2 valores numéricos.
     *
     * @param a primeiro valor numérico.
     * @param b segundo valor numérico.
     * @return valor de {@code a} se {@code b == 0},
     *         caso contrário retorna a função recursiva com {@code b} e {@code a % b}.
     */
    private static long greatestCommonDivisor(long a, long b) {
        return b == 0 ? Math.abs(a) : greatestCommonDivisor(b, a % b);
    }
}
