package com.lutavafs.linearprogramming.solver;

import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;
import com.lutavafs.linearprogramming.domain.simplex.model.Iteration;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.lutavafs.linearprogramming.util.SimplexAsserts.assertMapEquals;
import static com.lutavafs.linearprogramming.util.SimplexTestFactories.getMaxExample;
import static com.lutavafs.linearprogramming.util.SimplexTestFactories.getMinExample;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class SimplexSolverIntegrationTests {

    @Test
    public void optimalMaxSimplexTest() {
        SimplexSolver solver = getMaxExample();
        SimplexResult result = solver.calculate();

        List<Iteration> iterations = List.of(
                new Iteration(
                        "Inicial",
                        new int[] {-1, -1},
                        new String[] {"Z", "s1", "a1", "a2"},
                        new String[] {"Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "-5", "-4", "-3", "-6", "0", "0", "M", "M", "0"},
                                {"0", "1", "1", "1", "1", "1", "0", "0", "0", "20"},
                                {"0", "2", "1", "3", "4", "0", "-1", "1", "0", "24"},
                                {"0", "1", "2", "0", "1", "0", "0", "0", "1", "15"}
                        }
                ),
                new Iteration(
                        "Iteração",
                        new int[] {4, 1},
                        new String[] {"Z", "s1", "a1", "a2"},
                        new String[] {"Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "-5-3M", "-4-3M", "-3-3M", "-6-5M", "0", "M", "0", "0", "-39M"},
                                {"0", "1", "1", "1", "1", "1", "0", "0", "0", "20"},
                                {"0", "2", "1", "3", "4", "0", "-1", "1", "0", "24"},
                                {"0", "1", "2", "0", "1", "0", "0", "0", "1", "15"}
                        }
                ),
                new Iteration(
                        "Iteração",
                        new int[] {2, 2},
                        new String[] {"Z", "s1", "x4", "a2"},
                        new String[] {"Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "-2-1/2M", "-5/2-7/4M", "3/2+3/4M", "0", "0", "-3/2-1/4M", "3/2+5/4M", "0", "36-9M"},
                                {"0", "1/2", "3/4", "1/4", "0", "1", "1/4", "-1/4", "0", "14"},
                                {"0", "1/2", "1/4", "3/4", "1", "0", "-1/4", "1/4", "0", "6"},
                                {"0", "1/2", "7/4", "-3/4", "0", "0", "1/4", "-1/4", "1", "9"}
                        }
                ),
                new Iteration(
                        "Iteração",
                        new int[] {1, 1},
                        new String[] {"Z", "s1", "x4", "x2"},
                        new String[] {"Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "-9/7", "0", "3/7", "0", "0", "-8/7", "8/7+M", "10/7+M", "342/7"},
                                {"0", "2/7", "0", "4/7", "0", "1", "1/7", "-1/7", "-3/7", "71/7"},
                                {"0", "3/7", "0", "6/7", "1", "0", "-2/7", "2/7", "-1/7", "33/7"},
                                {"0", "2/7", "1", "-3/7", "0", "0", "1/7", "-1/7", "4/7", "36/7"}
                        }
                ),
                new Iteration(
                        "Iteração",
                        new int[] {6, 2},
                        new String[] {"Z", "s1", "x1", "x2"},
                        new String[] {"Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "0", "0", "3", "3", "0", "-2", "2+M", "1+M", "63"},
                                {"0", "0", "0", "0", "-2/3", "1", "1/3", "-1/3", "-1/3", "7"},
                                {"0", "1", "0", "2", "7/3", "0", "-2/3", "2/3", "-1/3", "11"},
                                {"0", "0", "1", "-1", "-2/3", "0", "1/3", "-1/3", "2/3", "2"}
                        }
                ),
                new Iteration(
                        "Iteração",
                        new int[] {3, 0},
                        new String[] {"Z", "s1", "x1", "s2"},
                        new String[] {"Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "0", "6", "-3", "-1", "0", "0", "M", "5+M", "75"},
                                {"0", "0", "-1", "1", "0", "1", "0", "0", "-1", "5"},
                                {"0", "1", "2", "0", "1", "0", "0", "0", "1", "15"},
                                {"0", "0", "3", "-3", "-2", "0", "1", "-1", "2", "6"}
                        }
                ),
                new Iteration(
                        "Iteração",
                        new int[] {4, 1},
                        new String[] {"Z", "x3", "x1", "s2"},
                        new String[] {"Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "0", "3", "0", "-1", "3", "0", "M", "2+M", "90"},
                                {"0", "0", "-1", "1", "0", "1", "0", "0", "-1", "5"},
                                {"0", "1", "2", "0", "1", "0", "0", "0", "1", "15"},
                                {"0", "0", "0", "0", "-2", "3", "1", "-1", "-1", "21"}
                        }
                ),
                new Iteration(
                        "Final",
                        new int[] {-1, -1},
                        new String[] {"Z", "x3", "x4", "s2"},
                        new String[] {"Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "1", "5", "0", "0", "3", "0", "M", "3+M", "105"},
                                {"0", "0", "-1", "1", "0", "1", "0", "0", "-1", "5"},
                                {"0", "1", "2", "0", "1", "0", "0", "0", "1", "15"},
                                {"0", "2", "4", "0", "0", "3", "1", "-1", "1", "51"}
                        }
                )
        );

        Map<String, String> variableValues = Map.of(
                "x1", "0",
                "x2", "0",
                "x3", "5",
                "x4", "15",
                "s1", "0",
                "s2", "51",
                "a1", "0",
                "a2", "0"
        );

        assertIterableEquals(iterations, result.getIterations());
        assertEquals(StatusResult.OPTIMAL.getTitle(), result.getStatus());
        assertEquals(105, result.getObjectiveValue());
        assertMapEquals(variableValues, result.getVariableValues());
    }

    @Test
    public void optimalMinSimplexTests() {
        SimplexSolver solver = getMinExample();
        SimplexResult result = solver.calculate();

        List<Iteration> iterations = List.of(
                new Iteration(
                        "Inicial",
                        new int[] {-1, -1},
                        new String[] {"-Z", "s1", "a1", "a2"},
                        new String[] {"-Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "3", "2", "5", "4", "0", "0", "M", "M", "0"},
                                {"0", "2", "1", "3", "1", "1", "0", "0", "0", "40"},
                                {"0", "1", "2", "1", "2", "0", "-1", "1", "0", "20"},
                                {"0", "3", "0", "2", "2", "0", "0", "0", "1", "30"}
                        }
                ),
                new Iteration(
                        "Iteração",
                        new int[] {1, 2},
                        new String[] {"-Z", "s1", "a1", "a2"},
                        new String[] {"-Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "3-4M", "2-2M", "5-3M", "4-4M", "0", "M", "0", "0", "-50M"},
                                {"0", "2", "1", "3", "1", "1", "0", "0", "0", "40"},
                                {"0", "1", "2", "1", "2", "0", "-1", "1", "0", "20"},
                                {"0", "3", "0", "2", "2", "0", "0", "0", "1", "30"}
                        }
                ),
                new Iteration(
                        "Iteração",
                        new int[] {2, 1},
                        new String[] {"-Z", "s1", "a1", "x1"},
                        new String[] {"-Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "0", "2-2M", "3-1/3M", "2-4/3M", "0", "M", "0", "-1+4/3M", "-30-10M"},
                                {"0", "0", "1", "5/3", "-1/3", "1", "0", "0", "-2/3", "20"},
                                {"0", "0", "2", "1/3", "4/3", "0", "-1", "1", "-1/3", "10"},
                                {"0", "1", "0", "2/3", "2/3", "0", "0", "0", "1/3", "10"}
                        }
                ),
                new Iteration(
                        "Final",
                        new int[] {-1, -1},
                        new String[] {"-Z", "s1", "x2", "x1"},
                        new String[] {"-Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"},
                        new String[][] {
                                {"1", "0", "0", "8/3", "2/3", "0", "1", "-1+M", "-2/3+M", "-40"},
                                {"0", "0", "0", "3/2", "-1", "1", "1/2", "-1/2", "-1/2", "15"},
                                {"0", "0", "1", "1/6", "2/3", "0", "-1/2", "1/2", "-1/6", "5"},
                                {"0", "1", "0", "2/3", "2/3", "0", "0", "0", "1/3", "10"}
                        }
                )
        );

        Map<String, String> variableValues = Map.of(
                "x1", "10",
                "x2", "5",
                "x3", "0",
                "x4", "0",
                "s1", "15",
                "s2", "0",
                "a1", "0",
                "a2", "0"
        );

        assertIterableEquals(iterations, result.getIterations());
        assertEquals(StatusResult.OPTIMAL.getTitle(), result.getStatus());
        assertEquals(40, result.getObjectiveValue());
        assertMapEquals(variableValues, result.getVariableValues());
    }
}