package com.lutavafs.linearprogramming.solver;

import com.lutavafs.linearprogramming.domain.simplex.model.BigMCoefficient;
import com.lutavafs.linearprogramming.domain.simplex.model.Iteration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.lutavafs.linearprogramming.util.SimplexAsserts.assertMatrixEquals;
import static com.lutavafs.linearprogramming.util.SimplexTestFactories.getMinExample;
import static org.junit.jupiter.api.Assertions.*;

public class SimplexSolverTests {

    @Test
    public void createTableauTest() {
        SimplexSolver solver = getMinExample();
        solver.createTableau();

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

        assertArrayEquals(rowZ, solver.getTableau().rowZ());
        assertMatrixEquals(matrix, solver.getTableau().matrix());
        assertArrayEquals(columnNames, solver.getTableau().columnNames());
        assertArrayEquals(basicIndexes, solver.getTableau().basicIndexes());
    }

    @Test
    public void resetZRowTest() {
        SimplexSolver solver = getMinExample();
        solver.createTableau();
        solver.resetZRow();

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

        assertArrayEquals(rowZ, solver.getTableau().rowZ());
    }

    @Test
    public void pivotTest() {
        SimplexSolver solver = getMinExample();
        solver.createTableau();
        solver.resetZRow();
        solver.findPivotColumn();
        solver.findPivotRow();
        solver.getTableau().pivot(solver.getPivotRow(), solver.getPivotColumn());

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

        assertEquals(1, solver.getPivotColumn());
        assertEquals(2, solver.getPivotRow());
        assertArrayEquals(rowZ, solver.getTableau().rowZ());
        assertMatrixEquals(matrix, solver.getTableau().matrix());
    }

    @Test
    public void iterateTest() {
        SimplexSolver solver = getMinExample();
        solver.createTableau();
        solver.resetZRow();
        solver.iterate();

        List<Iteration> iterations = List.of(
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

        assertIterableEquals(iterations, solver.getIterations());
    }
}
