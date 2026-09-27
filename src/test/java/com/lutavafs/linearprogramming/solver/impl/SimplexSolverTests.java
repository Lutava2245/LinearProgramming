package com.lutavafs.linearprogramming.solver.impl;

import com.lutavafs.linearprogramming.domain.simplex.model.BigMCoefficient;
import com.lutavafs.linearprogramming.domain.simplex.model.Iteration;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexProblem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.lutavafs.linearprogramming.util.SimplexAsserts.assertMatrixEquals;
import static com.lutavafs.linearprogramming.util.SimplexTestFactory.getMinExample;
import static org.junit.jupiter.api.Assertions.*;

public class SimplexSolverTests {

    private final SimplexSolver solver = new SimplexSolver();

    @Test
    public void createTableauTest() {
        SimplexProblem problem = getMinExample();
        solver.createTableau(problem);

        BigMCoefficient[] rowZ = {
                new BigMCoefficient(1, 0),
                new BigMCoefficient(3, 0),
                new BigMCoefficient(2, 0),
                new BigMCoefficient(5, 0),
                new BigMCoefficient(4, 0),
                new BigMCoefficient(0, 0),
                new BigMCoefficient(0, 0),
                new BigMCoefficient(0, 1),
                new BigMCoefficient(0, 1),
                new BigMCoefficient(0, 0)
        };
        double[][] matrix = {
                {0, 2, 1, 3, 1, 1, 0, 0, 0, 40},
                {0, 1, 2, 1, 2, 0, -1, 1, 0, 20},
                {0, 3, 0, 2, 2, 0, 0, 0, 1, 30}
        };
        String[] columnNames = {"-Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS"};
        int[] basicIndexes = {5, 7, 8};

        assertArrayEquals(rowZ, problem.getTableau().getRowZ());
        assertMatrixEquals(matrix, problem.getTableau().getMatrix());
        assertArrayEquals(columnNames, problem.getTableau().getColumnNames());
        assertArrayEquals(basicIndexes, problem.getTableau().getBasicIndexes());
    }

    @Test
    public void resetZRowTest() {
        SimplexProblem problem = getMinExample();
        solver.createTableau(problem);
        solver.resetZRow(problem.getTableau());

        BigMCoefficient[] rowZ = {
                new BigMCoefficient(1, 0),
                new BigMCoefficient(3, -4),
                new BigMCoefficient(2, -2),
                new BigMCoefficient(5, -3),
                new BigMCoefficient(4, -4),
                new BigMCoefficient(0, 0),
                new BigMCoefficient(0, 1),
                new BigMCoefficient(0, 0),
                new BigMCoefficient(0, 0),
                new BigMCoefficient(0, -50)
        };

        assertArrayEquals(rowZ, problem.getTableau().getRowZ());
    }

    @Test
    public void pivotTest() {
        SimplexProblem problem = getMinExample();
        solver.createTableau(problem);
        solver.resetZRow(problem.getTableau());
        solver.findPivotColumn(problem.getTableau());
        solver.findPivotRow(problem.getTableau());
        problem.getTableau().pivot();

        BigMCoefficient[] rowZ = {
                new BigMCoefficient(1, 0),
                new BigMCoefficient(0, 0),
                new BigMCoefficient(2, -2),
                new BigMCoefficient(3, -1.0/3),
                new BigMCoefficient(2, -4.0/3),
                new BigMCoefficient(0, 0),
                new BigMCoefficient(0, 1),
                new BigMCoefficient(0, 0),
                new BigMCoefficient(-1, 4.0/3),
                new BigMCoefficient(-30, -10)
        };
        double[][] matrix = {
                {0, 0, 1, 5.0/3, -1.0/3, 1, 0, 0, -2.0/3, 20},
                {0, 0, 2, 1.0/3, 4.0/3, 0, -1, 1, -1.0/3, 10},
                {0, 1, 0, 2.0/3, 2.0/3, 0, 0, 0, 1.0/3, 10}
        };

        assertEquals(1, problem.getTableau().getPivotColumn());
        assertEquals(2, problem.getTableau().getPivotRow());
        assertArrayEquals(rowZ, problem.getTableau().getRowZ());
        assertMatrixEquals(matrix, problem.getTableau().getMatrix());
    }

    @Test
    public void iterateTest() {
        SimplexProblem problem = getMinExample();
        List<Iteration> iterations = new ArrayList<>();

        solver.createTableau(problem);
        solver.resetZRow(problem.getTableau());
        solver.iterate(problem, iterations);

        List<Iteration> iterationsExpected = List.of(
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
                )
        );

        assertIterableEquals(iterationsExpected, iterations);
    }
}
