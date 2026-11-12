package com.lutavafs.linearprogramming.exception;

import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;
import com.lutavafs.linearprogramming.domain.simplex.model.Iteration;
import lombok.Getter;

import java.util.List;

/**
 * Exceção para erros de loop no algoritmo de Simplex em casos de degeneração.
 *
 * @see StatusResult#DEGENERATE_CYCLING
 */
@Getter
public class SimplexConvergenceException extends RuntimeException {
    /**
     * Iterações do Tableau feitas durante o processo de Simplex antes da exceção ser lançada.
     */
    private final List<Iteration> iterations;

    /**
     * Construtor da exceção de convergência do Simplex.
     * 
     * @param iterations Iterações do Tableau feitas durante o processo de Simplex antes da exceção ser lançada.
     */
    public SimplexConvergenceException(List<Iteration> iterations) {
        super("O algoritmo atingiu o limite de iterações sem convergir");
        this.iterations = iterations;
    }

}
