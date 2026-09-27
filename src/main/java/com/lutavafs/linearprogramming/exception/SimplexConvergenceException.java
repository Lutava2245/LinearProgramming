package com.lutavafs.linearprogramming.exception;

public class SimplexConvergenceException extends RuntimeException {

    public SimplexConvergenceException() {
        super("O algoritmo atingiu o limite de iterações sem convergir");
    }
}
