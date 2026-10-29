package com.lutavafs.linearprogramming.solver;

/**
 * Interface que, ao ser implementada, contém os cálculos dos algoritmos.
 *
 * @param <P> classe representando um problema que contém os dados.
 * @param <R> classe representando o resultado após os cálculos.
 */
public interface Solver<P, R> {
    R calculate(P problem);
}
