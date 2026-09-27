package com.lutavafs.linearprogramming.domain.simplex.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
