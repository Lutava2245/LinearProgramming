package com.lutavafs.linearprogramming.domain.simplex.model;

public record BigMCoefficient(double value, double bigM) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigMCoefficient coefficient = (BigMCoefficient) o;
        return Math.abs(this.bigM - coefficient.bigM) < 1e-9 && Math.abs(this.value - coefficient.value) < 1e-9;
    }

}