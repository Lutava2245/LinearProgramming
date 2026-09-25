package com.lutavafs.linearprogramming.domain.simplex.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusResult {
    OPTIMAL("Solução ótima"),
    FEASIBLE("Viável"),
    INFEASIBLE("Inviável");

    final String title;
}
