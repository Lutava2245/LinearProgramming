package com.lutavafs.linearprogramming.util;

public class MathFormater {
    private static final double EPSILON = 1e-9;

    public static String formatDouble(double valor) {
        if (Double.isNaN(valor) || Double.isInfinite(valor))
            return String.valueOf(valor);

        if (Math.abs(valor) < 1e-9) {
            return "0";
        }

        for (int d = 1; d <= 10000; d++) {
            long n = Math.round(valor * d);

            if (Math.abs((double) n / d - valor) < EPSILON) {
                long mdc = mdc(n, d);
                long numSimplificado = n / mdc;
                long denSimplificado = d / mdc;

                return denSimplificado == 1 ? String.valueOf(numSimplificado) : numSimplificado + "/" + denSimplificado;
            }
        }

        return String.valueOf(valor);
    }

    private static long mdc(long a, long b) {
        return b == 0 ? Math.abs(a) : mdc(b, a % b);
    }
}
