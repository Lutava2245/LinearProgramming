package com.lutavafs.linearprogramming.solver;

public interface Solver<P, R> {
    R calculate(P problem);
}
