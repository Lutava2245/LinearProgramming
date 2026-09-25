package com.lutavafs.linearprogramming.domain.simplex.model;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Constraint {
    private double[] coefficients;
    private ConstraintType constraintType;
    private double rightHandValue;
}
