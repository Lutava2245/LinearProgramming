package com.lutavafs.linearprogramming.util;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import com.lutavafs.linearprogramming.domain.simplex.enums.OptimizationType;
import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;
import com.lutavafs.linearprogramming.domain.simplex.model.Constraint;
import com.lutavafs.linearprogramming.domain.simplex.model.Iteration;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexProblem;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.solver.impl.SimplexSolver;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Fábrica de testes para o {@link SimplexSolver}, fornecendo exemplos de
 * problemas e
 * resultados esperados para diferentes cenários.
 */
public class SimplexTestFactory {

    /**
     * Cria um problema de programação linear de maximização para teste.
     * <ul>
     * <li>Max Z = 5x₁ + 4x₂ + 3x₃ + 6x₄</li>
     * <li>x₁ + x₂ + x₃ + x₄ &#8804; 20</li>
     * <li>2x₁ + x₂ + 3x₃ + 4x₄ &#8805; 24</li>
     * <li>x₁ + 2x₂ + x₄ = 15</li>
     * </ul>
     *
     * @return problema de maximização.
     */
    public static @NonNull SimplexProblem getMaxExample() {
        SimplexProblem problem = new SimplexProblem();
        problem.setOptimizationType(OptimizationType.MAXIMIZATION);

        problem.setObjectiveFunction(new Double[] { 5.0, 4.0, 3.0, 6.0 });

        problem.setConstraints(
                List.of(
                        new Constraint(new double[] { 1.0, 1.0, 1.0, 1.0 }, ConstraintType.LESS_EQUAL, 20.0),
                        new Constraint(new double[] { 2.0, 1.0, 3.0, 4.0 }, ConstraintType.GREATER_EQUAL, 24.0),
                        new Constraint(new double[] { 1.0, 2.0, 0.0, 1.0 }, ConstraintType.EQUAL, 15.0)));
        return problem;
    }

    /**
     * Cria o resultado esperado para o problema de maximização de teste.
     * 
     * @return resultado esperado do problema de maximização.
     */
    public static @NonNull SimplexResult getMaxExampleResult() {
        return new SimplexResult(
                List.of(
                        new Iteration(
                                "Inicial",
                                new int[] { -1, -1 },
                                new String[] { "Z", "s1", "a1", "a2" },
                                new String[] { "Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "-5", "-4", "-3", "-6", "0", "0", "M", "M", "0" },
                                        { "0", "1", "1", "1", "1", "1", "0", "0", "0", "20" },
                                        { "0", "2", "1", "3", "4", "0", "-1", "1", "0", "24" },
                                        { "0", "1", "2", "0", "1", "0", "0", "0", "1", "15" }
                                }),
                        new Iteration(
                                "Iteração 1",
                                new int[] { 4, 1 },
                                new String[] { "Z", "s1", "a1", "a2" },
                                new String[] { "Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "-5-3M", "-4-3M", "-3-3M", "-6-5M", "0", "M", "0", "0", "-39M" },
                                        { "0", "1", "1", "1", "1", "1", "0", "0", "0", "20" },
                                        { "0", "2", "1", "3", "4", "0", "-1", "1", "0", "24" },
                                        { "0", "1", "2", "0", "1", "0", "0", "0", "1", "15" }
                                }),
                        new Iteration(
                                "Iteração 2",
                                new int[] { 2, 2 },
                                new String[] { "Z", "s1", "x4", "a2" },
                                new String[] { "Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "-2-1/2M", "-5/2-7/4M", "3/2+3/4M", "0", "0", "-3/2-1/4M", "3/2+5/4M", "0", "36-9M" },
                                        { "0", "1/2", "3/4", "1/4", "0", "1", "1/4", "-1/4", "0", "14" },
                                        { "0", "1/2", "1/4", "3/4", "1", "0", "-1/4", "1/4", "0", "6" },
                                        { "0", "1/2", "7/4", "-3/4", "0", "0", "1/4", "-1/4", "1", "9" }
                                }),
                        new Iteration(
                                "Iteração 3",
                                new int[] { 1, 1 },
                                new String[] { "Z", "s1", "x4", "x2" },
                                new String[] { "Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "-9/7", "0", "3/7", "0", "0", "-8/7", "8/7+M", "10/7+M", "342/7" },
                                        { "0", "2/7", "0", "4/7", "0", "1", "1/7", "-1/7", "-3/7", "71/7" },
                                        { "0", "3/7", "0", "6/7", "1", "0", "-2/7", "2/7", "-1/7", "33/7" },
                                        { "0", "2/7", "1", "-3/7", "0", "0", "1/7", "-1/7", "4/7", "36/7" }
                                }),
                        new Iteration(
                                "Iteração 4",
                                new int[] { 6, 2 },
                                new String[] { "Z", "s1", "x1", "x2" },
                                new String[] { "Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "0", "0", "3", "3", "0", "-2", "2+M", "1+M", "63" },
                                        { "0", "0", "0", "0", "-2/3", "1", "1/3", "-1/3", "-1/3", "7" },
                                        { "0", "1", "0", "2", "7/3", "0", "-2/3", "2/3", "-1/3", "11" },
                                        { "0", "0", "1", "-1", "-2/3", "0", "1/3", "-1/3", "2/3", "2" }
                                }),
                        new Iteration(
                                "Iteração 5",
                                new int[] { 3, 0 },
                                new String[] { "Z", "s1", "x1", "s2" },
                                new String[] { "Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "0", "6", "-3", "-1", "0", "0", "M", "5+M", "75" },
                                        { "0", "0", "-1", "1", "0", "1", "0", "0", "-1", "5" },
                                        { "0", "1", "2", "0", "1", "0", "0", "0", "1", "15" },
                                        { "0", "0", "3", "-3", "-2", "0", "1", "-1", "2", "6" }
                                }),
                        new Iteration(
                                "Iteração 6",
                                new int[] { 4, 1 },
                                new String[] { "Z", "x3", "x1", "s2" },
                                new String[] { "Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "0", "3", "0", "-1", "3", "0", "M", "2+M", "90" },
                                        { "0", "0", "-1", "1", "0", "1", "0", "0", "-1", "5" },
                                        { "0", "1", "2", "0", "1", "0", "0", "0", "1", "15" },
                                        { "0", "0", "0", "0", "-2", "3", "1", "-1", "-1", "21" }
                                }),
                        new Iteration(
                                "Final",
                                new int[] { -1, -1 },
                                new String[] { "Z", "x3", "x4", "s2" },
                                new String[] { "Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "1", "5", "0", "0", "3", "0", "M", "3+M", "105" },
                                        { "0", "0", "-1", "1", "0", "1", "0", "0", "-1", "5" },
                                        { "0", "1", "2", "0", "1", "0", "0", "0", "1", "15" },
                                        { "0", "2", "4", "0", "0", "3", "1", "-1", "1", "51" }
                                })),
                StatusResult.OPTIMAL.getTitle(),
                OptimizationType.MAXIMIZATION.getTitle(),
                "105",
                Map.of(
                        "x1", "0",
                        "x2", "0",
                        "x3", "5",
                        "x4", "15",
                        "s1", "0",
                        "s2", "51",
                        "a1", "0",
                        "a2", "0"));
    }

    /**
     * Cria um problema de programação linear de minimização para teste.
     * <ul>
     * <li>Min Z = 3x₁ + 2x₂ + 5x₃ + 4x₄</li>
     * <li>2x₁ + x₂ + 3x₃ + x₄ &#8804; 40</li>
     * <li>x₁ + 2x₂ + x₃ + 2x₄ &#8805; 20</li>
     * <li>3x₁ + 2x₃ + 2x₄ = 30</li>
     * </ul>
     * 
     * @return problema de minimização.
     */
    public static @NonNull SimplexProblem getMinExample() {
        SimplexProblem problem = new SimplexProblem();
        problem.setOptimizationType(OptimizationType.MINIMIZATION);

        problem.setObjectiveFunction(new Double[] { 3.0, 2.0, 5.0, 4.0 });

        problem.setConstraints(
                List.of(
                        new Constraint(new double[] { 2.0, 1.0, 3.0, 1.0 }, ConstraintType.LESS_EQUAL, 40.0),
                        new Constraint(new double[] { 1.0, 2.0, 1.0, 2.0 }, ConstraintType.GREATER_EQUAL, 20.0),
                        new Constraint(new double[] { 3.0, 0.0, 2.0, 2.0 }, ConstraintType.EQUAL, 30.0)));
        return problem;
    }

    /**
     * Cria o resultado esperado para o problema de minimização de teste.
     * 
     * @return resultado esperado do problema de minimização.
     */
    public static @NonNull SimplexResult getMinExampleResult() {
        return new SimplexResult(
                List.of(
                        new Iteration(
                                "Inicial",
                                new int[] { -1, -1 },
                                new String[] { "-Z", "s1", "a1", "a2" },
                                new String[] { "-Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "3", "2", "5", "4", "0", "0", "M", "M", "0" },
                                        { "0", "2", "1", "3", "1", "1", "0", "0", "0", "40" },
                                        { "0", "1", "2", "1", "2", "0", "-1", "1", "0", "20" },
                                        { "0", "3", "0", "2", "2", "0", "0", "0", "1", "30" }
                                }),
                        new Iteration(
                                "Iteração 1",
                                new int[] { 1, 2 },
                                new String[] { "-Z", "s1", "a1", "a2" },
                                new String[] { "-Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "3-4M", "2-2M", "5-3M", "4-4M", "0", "M", "0", "0", "-50M" },
                                        { "0", "2", "1", "3", "1", "1", "0", "0", "0", "40" },
                                        { "0", "1", "2", "1", "2", "0", "-1", "1", "0", "20" },
                                        { "0", "3", "0", "2", "2", "0", "0", "0", "1", "30" }
                                }),
                        new Iteration(
                                "Iteração 2",
                                new int[] { 2, 1 },
                                new String[] { "-Z", "s1", "a1", "x1" },
                                new String[] { "-Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "0", "2-2M", "3-1/3M", "2-4/3M", "0", "M", "0", "-1+4/3M", "-30-10M" },
                                        { "0", "0", "1", "5/3", "-1/3", "1", "0", "0", "-2/3", "20" },
                                        { "0", "0", "2", "1/3", "4/3", "0", "-1", "1", "-1/3", "10" },
                                        { "0", "1", "0", "2/3", "2/3", "0", "0", "0", "1/3", "10" }
                                }),
                        new Iteration(
                                "Final",
                                new int[] { -1, -1 },
                                new String[] { "-Z", "s1", "x2", "x1" },
                                new String[] { "-Z", "x1", "x2", "x3", "x4", "s1", "s2", "a1", "a2", "RHS" },
                                new String[][] {
                                        { "1", "0", "0", "8/3", "2/3", "0", "1", "-1+M", "-2/3+M", "-40" },
                                        { "0", "0", "0", "3/2", "-1", "1", "1/2", "-1/2", "-1/2", "15" },
                                        { "0", "0", "1", "1/6", "2/3", "0", "-1/2", "1/2", "-1/6", "5" },
                                        { "0", "1", "0", "2/3", "2/3", "0", "0", "0", "1/3", "10" }
                                })),
                StatusResult.OPTIMAL.getTitle(),
                OptimizationType.MINIMIZATION.getTitle(),
                "40",
                Map.of(
                        "x1", "10",
                        "x2", "5",
                        "x3", "0",
                        "x4", "0",
                        "s1", "15",
                        "s2", "0",
                        "a1", "0",
                        "a2", "0"));
    }

    /**
     * Cria um problema de programação linear com múltiplas soluções ótimas para
     * teste.
     * <ul>
     * <li>Max Z = 2x₁ + 4x₂</li>
     * <li>x₁ + 2x₂ &#8804; 10</li>
     * <li>x₁ + x₂ &#8804; 6</li>
     * </ul>
     * 
     * @return problema com múltiplas soluções ótimas.
     */
    public static @NonNull SimplexProblem getMultipleOptimalExample() {
        SimplexProblem problem = new SimplexProblem();
        problem.setOptimizationType(OptimizationType.MAXIMIZATION);

        problem.setObjectiveFunction(new Double[] { 2.0, 4.0 });

        problem.setConstraints(
                List.of(
                        new Constraint(new double[] { 1.0, 2.0 }, ConstraintType.LESS_EQUAL, 10.0),
                        new Constraint(new double[] { 1.0, 1.0 }, ConstraintType.LESS_EQUAL, 6.0)));
        return problem;
    }

    /**
     * Cria o resultado esperado para o problema com múltiplas soluções ótimas de
     * teste.
     * 
     * @return resultado esperado do problema com múltiplas soluções ótimas.
     */
    public static @NonNull SimplexResult getMultipleOptimalExampleResult() {
        return new SimplexResult(
                List.of(
                        new Iteration(
                                "Inicial",
                                new int[] { -1, -1 },
                                new String[] { "Z", "s1", "s2" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "RHS" },
                                new String[][] {
                                        { "1", "-2", "-4", "0", "0", "0" },
                                        { "0", "1", "2", "1", "0", "10" },
                                        { "0", "1", "1", "0", "1", "6" }
                                }),
                        new Iteration(
                                "Iteração 1",
                                new int[] { 2, 0 },
                                new String[] { "Z", "s1", "s2" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "RHS" },
                                new String[][] {
                                        { "1", "-2", "-4", "0", "0", "0" },
                                        { "0", "1", "2", "1", "0", "10" },
                                        { "0", "1", "1", "0", "1", "6" }
                                }),
                        new Iteration(
                                "Final",
                                new int[] { -1, -1 },
                                new String[] { "Z", "x2", "s2" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "RHS" },
                                new String[][] {
                                        { "1", "0", "0", "2", "0", "20" },
                                        { "0", "1/2", "1", "1/2", "0", "5" },
                                        { "0", "1/2", "0", "-1/2", "1", "1" }
                                })),
                StatusResult.MULTIPLE_OPTIMAL.getTitle(),
                OptimizationType.MAXIMIZATION.getTitle(),
                "20",
                Map.of(
                        "x1", "0",
                        "x2", "5",
                        "s1", "0",
                        "s2", "1"));
    }

    /**
     * Cria um problema de programação linear inviável para teste.
     * <ul>
     * <li>Max Z = 3x₁ + 2x₂</li>
     * <li>x₁ + x₂ &#8804; 5</li>
     * <li>x₁ + x₂ &#8805; 10</li>
     * </ul>
     * 
     * @return problema inviável.
     */
    public static @NonNull SimplexProblem getInfeasibleExample() {
        SimplexProblem problem = new SimplexProblem();
        problem.setOptimizationType(OptimizationType.MAXIMIZATION);

        problem.setObjectiveFunction(new Double[] { 3.0, 2.0 });

        problem.setConstraints(
                List.of(
                        new Constraint(new double[] { 1.0, 1.0 }, ConstraintType.LESS_EQUAL, 5.0),
                        new Constraint(new double[] { 1.0, 1.0 }, ConstraintType.GREATER_EQUAL, 10.0)));
        return problem;
    }

    /**
     * Cria o resultado esperado para o problema inviável de teste.
     * 
     * @return resultado esperado do problema inviável.
     */
    public static @NonNull SimplexResult getInfeasibleExampleResult() {
        return new SimplexResult(
                List.of(
                        new Iteration(
                                "Inicial",
                                new int[] { -1, -1 },
                                new String[] { "Z", "s1", "a1" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "a1", "RHS" },
                                new String[][] {
                                        { "1", "-3", "-2", "0", "0", "M", "0" },
                                        { "0", "1", "1", "1", "0", "0", "5" },
                                        { "0", "1", "1", "0", "-1", "1", "10" }
                                }),
                        new Iteration(
                                "Iteração 1",
                                new int[] { 1, 0 },
                                new String[] { "Z", "s1", "a1" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "a1", "RHS" },
                                new String[][] {
                                        { "1", "-3-M", "-2-M", "0", "M", "0", "-10M" },
                                        { "0", "1", "1", "1", "0", "0", "5" },
                                        { "0", "1", "1", "0", "-1", "1", "10" }
                                }),
                        new Iteration(
                                "Final",
                                new int[] { -1, -1 },
                                new String[] { "Z", "x1", "a1" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "a1", "RHS" },
                                new String[][] {
                                        { "1", "0", "1", "3+M", "M", "0", "15-5M" },
                                        { "0", "1", "1", "1", "0", "0", "5" },
                                        { "0", "0", "0", "-1", "-1", "1", "5" }
                                })),
                StatusResult.INFEASIBLE.getTitle(),
                OptimizationType.MAXIMIZATION.getTitle(),
                "0",
                Map.of(
                        "x1", "5",
                        "x2", "0",
                        "s1", "0",
                        "s2", "0",
                        "a1", "5"));
    }

    /**
     * Cria um problema de programação linear ilimitado para teste.
     * <ul>
     * <li>Max Z = 2x₁ + x₂</li>
     * <li>x₁ - x₂ &#8804; 10</li>
     * <li>2x₁ - x₂ &#8804; 40</li>
     * </ul>
     * 
     * @return problema ilimitado.
     */
    public static @NonNull SimplexProblem getUnboundedExample() {
        SimplexProblem problem = new SimplexProblem();
        problem.setOptimizationType(OptimizationType.MAXIMIZATION);

        problem.setObjectiveFunction(new Double[] { 2.0, 1.0 });

        problem.setConstraints(
                List.of(
                        new Constraint(new double[] { 1.0, -1.0 }, ConstraintType.LESS_EQUAL, 10.0),
                        new Constraint(new double[] { 2.0, -1.0 }, ConstraintType.LESS_EQUAL, 40.0)));
        return problem;
    }

    /**
     * Cria o resultado esperado para o problema ilimitado de teste.
     * 
     * @return resultado esperado do problema ilimitado.
     */
    public static @NonNull SimplexResult getUnboundedExampleResult() {
        return new SimplexResult(
                List.of(
                        new Iteration(
                                "Inicial",
                                new int[] { -1, -1 },
                                new String[] { "Z", "s1", "s2" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "RHS" },
                                new String[][] {
                                        { "1", "-2", "-1", "0", "0", "0" },
                                        { "0", "1", "-1", "1", "0", "10" },
                                        { "0", "2", "-1", "0", "1", "40" }
                                }),
                        new Iteration(
                                "Iteração 1",
                                new int[] { 1, 0 },
                                new String[] { "Z", "s1", "s2" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "RHS" },
                                new String[][] {
                                        { "1", "-2", "-1", "0", "0", "0" },
                                        { "0", "1", "-1", "1", "0", "10" },
                                        { "0", "2", "-1", "0", "1", "40" }
                                }),
                        new Iteration(
                                "Iteração 2",
                                new int[] { 2, 1 },
                                new String[] { "Z", "x1", "s2" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "RHS" },
                                new String[][] {
                                        { "1", "0", "-3", "2", "0", "20" },
                                        { "0", "1", "-1", "1", "0", "10" },
                                        { "0", "0", "1", "-2", "1", "20" }
                                }),
                        new Iteration(
                                "Final",
                                new int[] { -1, -1 },
                                new String[] { "Z", "x1", "x2" },
                                new String[] { "Z", "x1", "x2", "s1", "s2", "RHS" },
                                new String[][] {
                                        { "1", "0", "0", "-4", "3", "80" },
                                        { "0", "1", "0", "-1", "1", "30" },
                                        { "0", "0", "1", "-2", "1", "20" }
                                })),
                StatusResult.UNBOUNDED.getTitle(),
                OptimizationType.MAXIMIZATION.getTitle(),
                "NA",
                Map.of(
                        "x1", "30",
                        "x2", "20",
                        "s1", "0",
                        "s2", "0"));
    }

    /**
     * Cria um problema de programação linear degenerado que pode causar ciclos para
     * teste.
     * <ul>
     * <li>Max Z = 10x₁ - 57x₂ - 9x₃ - 24x₄</li>
     * <li>0.5x₁ - 5.5x₂ - 2.5x₃ + 9x₄ &#8804; 0</li>
     * <li>0.5x₁ - 1.5x₂ - 0.5x₃ + 1x₄ &#8804; 0</li>
     * <li>x₁ &#8804; 1</li>
     * </ul>
     * <p>
     * Este problema é conhecido por ser degenerado e pode levar a ciclos durante a
     * execução do algoritmo Simplex.
     * </p>
     * 
     * @return problema degenerado que pode causar ciclos.
     */
    public static @NonNull SimplexProblem getDegenerateCyclingExample() {
        SimplexProblem problem = new SimplexProblem();
        problem.setOptimizationType(OptimizationType.MAXIMIZATION);

        problem.setObjectiveFunction(new Double[] { 10.0, -57.0, -9.0, -24.0 });

        problem.setConstraints(
                List.of(
                        new Constraint(new double[] { 0.5, -5.5, -2.5, 9.0 }, ConstraintType.LESS_EQUAL, 0.0),
                        new Constraint(new double[] { 0.5, -1.5, -0.5, 1.0 }, ConstraintType.LESS_EQUAL, 0.0),
                        new Constraint(new double[] { 1.0, 0.0, 0.0, 0.0 }, ConstraintType.LESS_EQUAL, 1.0)));
        return problem;
    }

    /**
     * Cria um problema de programação linear com lado direito negativo para teste.
     * <ul>
     * <li>Min Z = x₁ + x₂ + x₃</li>
     * <li>-x₁ &#8804; -2</li>
     * <li>-x₂ = -1</li>
     * <li>-x₃ &#8805; -4</li>
     * </ul>
     * <p>
     * O simplex não permite que o lado direito de uma restrição seja negativo, e este
     * problema testa a capacidade do algoritmo de converter essas restrições.
     * </p>
     * 
     * @return problema com lado direito negativo.
     */
    public static @NonNull SimplexProblem getNegativeRhsExample() {
        SimplexProblem problem = new SimplexProblem();
        problem.setOptimizationType(OptimizationType.MINIMIZATION);

        problem.setObjectiveFunction(new Double[] { 1.0, 1.0, 1.0 });

        problem.setConstraints(
                List.of(
                        new Constraint(new double[] { -1.0, 0.0, 0.0 }, ConstraintType.LESS_EQUAL, -2.0),
                        new Constraint(new double[] { 0.0, -1.0, 0.0 }, ConstraintType.EQUAL, -1.0),
                        new Constraint(new double[] { 0.0, 0.0, -1.0 }, ConstraintType.GREATER_EQUAL, -4.0)));
        return problem;
    }

    /**
     * Cria um problema de programação linear com solução inviável para teste.
     * <ul>
     * <li>Max Z = x₁</li>
     * <li>x₂ &#8804; 1</li>
     * <li>x₂ &#8805; 2</li>
     * </ul>
     * <p>
     * Por ser maximização e x1 não possuir restrições, a solução aparenta ser
     * ilimitado. Entretanto, as restrições de {@code x2} são impossíveis
     * matematicamente, tornando o problema inviável.
     * </p>
     * 
     * @return problema com solução inviável.
     */
    public static @NonNull SimplexProblem getFalseUnboundedExample() {
        SimplexProblem problem = new SimplexProblem();
        problem.setOptimizationType(OptimizationType.MAXIMIZATION);

        problem.setObjectiveFunction(new Double[] { 1.0, 0.0 });

        problem.setConstraints(
                List.of(
                        new Constraint(new double[] { 0.0, 1.0 }, ConstraintType.LESS_EQUAL, 1),
                        new Constraint(new double[] { 0.0, 1.0 }, ConstraintType.GREATER_EQUAL, 2)));
        return problem;
    }
}
