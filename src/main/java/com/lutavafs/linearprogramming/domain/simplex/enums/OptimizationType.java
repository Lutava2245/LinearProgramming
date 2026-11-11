package com.lutavafs.linearprogramming.domain.simplex.enums;

import com.lutavafs.linearprogramming.domain.simplex.model.SimplexProblem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Representa os tipos de otimização do simplex de uma {@link SimplexProblem}.
 * <p>
 * Os tipos de otimização são:
 * <ul>
 * <li>{@link #MAXIMIZATION}: Um problema de Maximização.</li>
 * <li>{@link #MINIMIZATION}: Um problema de Minimização.</li>
 * </ul>
 * <p>
 */
@RequiredArgsConstructor
@Getter
public enum OptimizationType {
    MAXIMIZATION("Maximização"),
    MINIMIZATION("Minimização");

    final String title;
}
