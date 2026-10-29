package com.lutavafs.linearprogramming.exception;

import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;

/**
 * Exceção para erros de loop no algortimo de Simplex em casos de degeneração.
 *
 * @see StatusResult#DEGENERATE_CYCLING
 */
public class SimplexConvergenceException extends RuntimeException {

    public SimplexConvergenceException() {
        super("O algoritmo atingiu o limite de iterações sem convergir");
    }

}
