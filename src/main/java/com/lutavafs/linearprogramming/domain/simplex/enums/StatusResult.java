package com.lutavafs.linearprogramming.domain.simplex.enums;

import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representa os resultados possíveis de um cálculo de Simplex atribuitos a
 * {@link SimplexResult}.
 * <p>
 * Os resultados possíveis são:
 * <ul>
 * <li>{@link #OPTIMAL}: Quando a solução é única e ótima.</li>
 * <li>{@link #MULTIPLE_OPTIMAL}: Quando há várias soluções ótimas
 * possíveis.</li>
 * <li>{@link #INFEASIBLE}: Quando não há soluções possíveis.</li>
 * <li>{@link #UNBOUNDED}: Quando a solução é ilimitada/infinita.</li>
 * <li>{@link #DEGENERATE_CYCLING}: Quando o cálculo entra em loop e não
 * encontra uma solução.</li>
 * </ul>
 * <p>
 */
@Getter
@AllArgsConstructor
public enum StatusResult {
    OPTIMAL("Solução ótima"),
    MULTIPLE_OPTIMAL("Múltiplas Soluções"),
    INFEASIBLE("Inviável"),
    UNBOUNDED("Ilimitado"),
    DEGENERATE_CYCLING("Degeneração");

    final String title;
}
