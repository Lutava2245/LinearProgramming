package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.util.MathFormater;
import jakarta.annotation.Nonnull;

import java.util.Objects;

public record BigMCoefficient(double value, double bigM) {
    public BigMCoefficient subtract(BigMCoefficient coefficient) {
        return new BigMCoefficient(this.value - coefficient.value(), this.bigM - coefficient.bigM());
    }

    public BigMCoefficient multiply(double num) {
        return new BigMCoefficient(this.value * num, this.bigM * num);
    }

    public int compareTo(BigMCoefficient coefficient) {
        if (Math.abs(this.bigM - coefficient.bigM) > 1e-9) {
            return Double.compare(this.bigM, coefficient.bigM);
        }
        return Double.compare(this.value, coefficient.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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