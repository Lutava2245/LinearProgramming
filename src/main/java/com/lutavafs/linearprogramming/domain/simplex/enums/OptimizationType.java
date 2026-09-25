package com.lutavafs.linearprogramming.domain.simplex.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OptimizationType {
    MAXIMIZATION("Maximização"),
    MINIMIZATION("Minimização");

    final String title;
}
