package com.lutavafs.linearprogramming.solver.impl;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.exception.SimplexConvergenceException;
import org.junit.jupiter.api.Test;

import static com.lutavafs.linearprogramming.util.SimplexAsserts.assertMapEquals;
import static com.lutavafs.linearprogramming.util.SimplexTestFactory.*;
import static org.junit.jupiter.api.Assertions.*;

public class SimplexSolverIntegrationTests {

    private final SimplexSolver solver = new SimplexSolver();

    @Test
    public void optimalMaxSimplexTest() {
        SimplexResult result = solver.calculate(getMaxExample());
        SimplexResult expectedResult = getMaxExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    public void optimalMinSimplexTest() {
        SimplexResult result = solver.calculate(getMinExample());
        SimplexResult expectedResult = getMinExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    public void multipleOptimalSimplexTest() {
        SimplexResult result = solver.calculate(getMultipleOptimalExample());
        SimplexResult expectedResult = getMultipleOptimalExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    public void infeasibleSimplexTest() {
        SimplexResult result = solver.calculate(getInfeasibleExample());
        SimplexResult expectedResult = getInfeasibleExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    public void unboundedSimplexTest() {
        SimplexResult result = solver.calculate(getUnboundedExample());
        SimplexResult expectedResult = getUnboundedExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    public void degenerateCyclingSimplexTest() {
        assertThrows(SimplexConvergenceException.class, () -> solver.calculate(getDegenerateCyclingExample()));
    }
}