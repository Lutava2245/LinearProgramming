package com.lutavafs.linearprogramming.solver.impl;

import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.exception.SimplexConvergenceException;
import org.junit.jupiter.api.Test;

import static com.lutavafs.linearprogramming.util.SimplexAsserts.assertMapEquals;
import static com.lutavafs.linearprogramming.util.SimplexTestFactory.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

/**
 * Testes de integração para a classe {@link SimplexSolver}.
 * <p>
 * Os testes verificam diferentes cenários de otimização linear, incluindo casos
 * de solução ótima, múltiplas soluções ótimas, inviabilidade, ilimitabilidade e
 * ciclo de degeneração.
 * </p>
 * 
 * @see SimplexResult
 */
public class SimplexSolverIntegrationTests {

    private final SimplexSolver solver = new SimplexSolver();

    @Test
    @DisplayName("Deve encontrar a solução ótima quando receber o problema de maximização")
    public void optimalMaxSimplexTest() {
        SimplexResult result = solver.calculate(getMaxExample());
        SimplexResult expectedResult = getMaxExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    @DisplayName("Deve encontrar a solução ótima quando receber o problema de minimização")
    public void optimalMinSimplexTest() {
        SimplexResult result = solver.calculate(getMinExample());
        SimplexResult expectedResult = getMinExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    @DisplayName("Deve encontrar múltiplas soluções ótimas quando receber o problema de múltiplas soluções ótimas")
    public void multipleOptimalSimplexTest() {
        SimplexResult result = solver.calculate(getMultipleOptimalExample());
        SimplexResult expectedResult = getMultipleOptimalExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    @DisplayName("Deve definir o problema como inviável quando receber o problema de inviabilidade")
    public void infeasibleSimplexTest() {
        SimplexResult result = solver.calculate(getInfeasibleExample());
        SimplexResult expectedResult = getInfeasibleExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    @DisplayName("Deve definir o problema como ilimitado quando receber o problema de ilimitabilidade")
    public void unboundedSimplexTest() {
        SimplexResult result = solver.calculate(getUnboundedExample());
        SimplexResult expectedResult = getUnboundedExampleResult();

        assertIterableEquals(expectedResult.getIterations(), result.getIterations());
        assertEquals(expectedResult.getStatus(), result.getStatus());
        assertEquals(expectedResult.getObjectiveValue(), result.getObjectiveValue());
        assertMapEquals(expectedResult.getVariableValues(), result.getVariableValues());
    }

    @Test
    @DisplayName("Deve lançar exceção de convergência quando receber o problema de ciclo de degeneração")
    public void degenerateCyclingSimplexTest() {
        assertThrows(SimplexConvergenceException.class, () -> solver.calculate(getDegenerateCyclingExample()));
    }

    @Test
    @DisplayName("Deve inverter restrição com RHS negativo e encontrar a solução ótima quando receber o problema com RHS negativo")
    public void negativeRhsSimplexTest() {
        SimplexResult result = solver.calculate(getNegativeRhsExample());

        assertNotNull(result);
        assertEquals("Solução ótima", result.getStatus());
        assertEquals("2", result.getVariableValues().get("x1"));
    }

    @Test
    @DisplayName("Deve definir a solução como inviável quando receber um problema com solução ilimitada falsa")
    public void falseUnboundedSimplexTest() {
        SimplexResult result = solver.calculate(getFalseUnboundedExample());

        assertNotNull(result);
        assertEquals("Inviável", result.getStatus());
    }
}