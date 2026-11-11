package com.lutavafs.linearprogramming.domain.simplex.enums;

import com.lutavafs.linearprogramming.domain.simplex.model.Constraint;

/**
 * Representa os tipos de restrição de uma {@link Constraint}.
 * <p>
 * Os tipos de restrição são:
 * <ul>
 * <li>{@link #LESS_EQUAL}: Uma restrição menor ou igual (&#8804).</li>
 * <li>{@link #GREATER_EQUAL}: Uma restrição maior ou igual (&#8805).</li>
 * <li>{@link #EQUAL}: Uma restrição de igualdade (=).</li>
 * </ul>
 * <p>
 */
public enum ConstraintType {
    LESS_EQUAL,
    GREATER_EQUAL,
    EQUAL
}
