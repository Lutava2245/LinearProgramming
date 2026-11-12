package com.lutavafs.linearprogramming.solver.impl;

import com.lutavafs.linearprogramming.domain.simplex.enums.ConstraintType;
import com.lutavafs.linearprogramming.domain.simplex.enums.OptimizationType;
import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;
import com.lutavafs.linearprogramming.domain.simplex.model.*;
import com.lutavafs.linearprogramming.exception.SimplexConvergenceException;
import com.lutavafs.linearprogramming.solver.Solver;
import com.lutavafs.linearprogramming.util.MathHelper;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Classe que realiza os cálculos do Simplex.
 */
@Service
public class SimplexSolver implements Solver<SimplexProblem, SimplexResult> {
    /**
     * Número máximo de iterações permitidas para o algoritmo Simplex para evitar
     * loops infinitos.
     * <p>
     * Em alguns casos, especialmente em problemas degenerados, o algoritmo
     * pode entrar em um loop infinito ou demorar muito para convergir para
     * uma solução ótima. Entretanto, é importante notar que atingir o limite
     * não garante que o problema seja degenerado. Em casos raros, o algoritmo
     * pode atingir o limite de iterações caso a solução ótima estiver longe do
     * ponto inicial ou se houver muitas variáveis e restrições.
     * </p>
     */
    private static final int MAX_ITERATIONS = 1000;

    /**
     * Calcula o resultado do Simplex para um problema de Simplex dado.
     * <p>
     * O cálculo do Simplex envolve a criação do {@link Tableau} inicial, a iteração
     * através de {@link Iteration}, e a determinação do {@link StatusResult}.
     * </p>
     * 
     * @param problem problema de Simplex a ser resolvido.
     * @return o resultado do Simplex, encapsulado em um {@link SimplexResult}.
     */
    @Override
    public SimplexResult calculate(SimplexProblem problem) {
        List<Iteration> iterations = new ArrayList<>();

        createTableau(problem);
        iterations.add(getIteration(problem, "Inicial"));

        if (problem.getTableau().haveArtificialVariables()) {
            resetZRow(problem.getTableau());
        }

        StatusResult status = iterate(problem, iterations);

        iterations.add(getIteration(problem, "Final"));
        return getResult(problem, iterations, status);
    }

    /**
     * Cria o {@link Tableau} inicial para o problema de Programação Linear no
     * Método Simplex.
     * <p>
     * O processo de construção compreende as seguintes etapas:
     * <ol>
     * <li><b>Normalização de RHS Negativo:</b> Se uma restrição possuir um valor de
     * lado direito ({@code RHS}) estritamente menor que zero, os coeficientes da
     * restrição e o {@code RHS} são multiplicados por {@code -1}, e o tipo da
     * restrição é invertido ({@code LESS_EQUAL} torna-se {@code GREATER_EQUAL} e
     * vice-versa; restrições {@code EQUAL} permanecem inalteradas).</li>
     * <li><b>Dimensionamento:</b> Determina-se a quantidade necessária de variáveis
     * de decisão ({@code x}), variáveis de folga/excesso ({@code s}) e variáveis
     * artificiais ({@code a}).</li>
     * <li><b>Montagem da Matriz:</b> O Tableau é preenchido com a função objetivo
     * na linha {@code Z} (com as variáveis de folga/artificiais adequadamente
     * ajustadas) e as restrições transformadas em equações com suas respectivas
     * variáveis básicas iniciais.</li>
     * </ol>
     * 
     * @param problem o problema de Programação Linear para o qual o tableau será
     *                criado.
     */
    protected void createTableau(SimplexProblem problem) {
        int numX = problem.getObjectiveFunction().length;
        int numS = 0;
        int numA = 0;

        for (Constraint c : problem.getConstraints()) {
            ConstraintType type = c.getConstraintType();
            if (c.getRightHandValue() < 0) {
                type = flip(type);
            }

            switch (type) {
                case LESS_EQUAL:
                    numS++;
                    break;

                case GREATER_EQUAL:
                    numS++;
                    numA++;
                    break;

                default:
                    numA++;
                    break;
            }
        }

        int totalColumns = 1 + numX + numS + numA + 1;
        int totalRows = problem.getConstraints().size();

        int[] basicIndexes = new int[totalRows];
        double[][] matrix = new double[totalRows][totalColumns];
        BigMCoefficient[] rowZ = new BigMCoefficient[totalColumns];
        String[] columnNames = new String[totalColumns];

        columnNames[0] = problem.getOptimizationType() == OptimizationType.MAXIMIZATION ? "Z" : "-Z";
        int colIdx = 1;
        for (int i = 1; i <= numX; i++) {
            columnNames[colIdx++] = "x" + i;
        }
        for (int i = 1; i <= numS; i++) {
            columnNames[colIdx++] = "s" + i;
        }
        for (int i = 1; i <= numA; i++) {
            columnNames[colIdx++] = "a" + i;
        }
        columnNames[totalColumns - 1] = "RHS";

        rowZ[0] = new BigMCoefficient(1, 0);
        for (int i = 0; i < numX; i++) {
            rowZ[1 + i] = new BigMCoefficient(
                    problem.getOptimizationType() == OptimizationType.MAXIMIZATION ? -problem.getObjectiveFunction()[i]
                            : problem.getObjectiveFunction()[i],
                    0);
        }
        for (int i = 0; i < numS; i++) {
            rowZ[1 + numX + i] = new BigMCoefficient(0, 0);
        }
        for (int i = 0; i < numA; i++) {
            rowZ[1 + numX + numS + i] = new BigMCoefficient(0, 1.0);
        }
        rowZ[totalColumns - 1] = new BigMCoefficient(0, 0);

        int sIdx = 1 + numX;
        int aIdx = 1 + numX + numS;

        for (int i = 0; i < problem.getConstraints().size(); i++) {
            Constraint c = problem.getConstraints().get(i);
            double[] coefficients = c.getCoefficients();
            ConstraintType type = c.getConstraintType();
            double rhs = c.getRightHandValue();

            if (rhs < 0) {
                coefficients = new double[c.getCoefficients().length];
                for (int j = 0; j < c.getCoefficients().length; j++) {
                    coefficients[j] = -c.getCoefficients()[j];
                }
                type = flip(type);
                rhs = -c.getRightHandValue();
            }

            matrix[i][0] = 0.0;

            System.arraycopy(coefficients, 0, matrix[i], 1, numX);

            if (type == ConstraintType.LESS_EQUAL) {
                matrix[i][sIdx] = 1.0;
                basicIndexes[i] = sIdx;
                sIdx++;
            } else if (type == ConstraintType.GREATER_EQUAL) {
                matrix[i][sIdx] = -1.0;
                matrix[i][aIdx] = 1.0;
                basicIndexes[i] = aIdx;
                sIdx++;
                aIdx++;
            } else if (type == ConstraintType.EQUAL) {
                matrix[i][aIdx] = 1.0;
                basicIndexes[i] = aIdx;
                aIdx++;
            }

            matrix[i][totalColumns - 1] = rhs;
        }

        problem.setTableau(new Tableau(
                rowZ,
                matrix,
                columnNames,
                basicIndexes));
    }

    private ConstraintType flip(ConstraintType type) {
        return switch (type) {
            case LESS_EQUAL -> ConstraintType.GREATER_EQUAL;
            case GREATER_EQUAL -> ConstraintType.LESS_EQUAL;
            case EQUAL -> ConstraintType.EQUAL;
        };
    }

    /**
     * Reinicia a linha Z do tableau.
     * <p>
     * A linha Z é recalculada subtraindo os valores das restrições correspondentes
     * das variáveis artificiais da linha Z. Assim, a linha Z reflete corretamente o
     * valor da função objetivo considerando as variáveis artificiais.
     * </p>
     * 
     * @param tableau a tabela Simplex cuja linha Z será reiniciada.
     */
    protected void resetZRow(Tableau tableau) {
        for (int i = 0; i < tableau.getBasicIndexes().length; i++) {
            if (tableau.getColumnNames()[tableau.getBasicIndexes()[i]].startsWith("a")) {
                for (int j = 0; j < tableau.getRowZ().length; j++) {
                    double valorRestricao = tableau.getMatrix()[i][j];
                    tableau.getRowZ()[j] = tableau.getRowZ()[j].subtract(new BigMCoefficient(0.0, valorRestricao));
                }
            }
        }
    }

    /**
     * Realiza uma iteração do método simplex.
     * <p>
     * O método Simplex funciona iterativamente, encontrando uma coluna pivô e uma
     * linha pivô, e então realizando a operação de pivotamento para atualizar o
     * tableau. O processo continua até que uma solução ótima seja encontrada ou até
     * que seja determinado que o problema é ilimitado.
     * </p>
     * 
     * @param problem    problema de Simplex a ser iterado.
     * @param iterations lista para armazenar as iterações do processo de Simplex.
     * @return o status do resultado após a iteração.
     * @see Tableau#pivot()
     * @see #findPivotColumn(Tableau)
     * @see #findPivotRow(Tableau)
     */
    protected StatusResult iterate(SimplexProblem problem, List<Iteration> iterations) {
        int currentIteration = 0;

        while (currentIteration < MAX_ITERATIONS) {
            BigMCoefficient lowestCoefficient = findPivotColumn(problem.getTableau());

            if (!lowestCoefficient.isStrictlyNegative())
                return StatusResult.OPTIMAL;

            findPivotRow(problem.getTableau());

            if (problem.getTableau().getPivotRow() == -1) {
                final int rightHandColumn = problem.getTableau().getColumnNames().length - 1;

                for (int i = 0; i < problem.getTableau().getBasicIndexes().length; i++) {
                    String variableName = problem.getTableau()
                            .getColumnNames()[problem.getTableau().getBasicIndexes()[i]];

                    if (variableName.startsWith("a")
                            && problem.getTableau().getMatrix()[i][rightHandColumn] > MathHelper.EPSILON) {
                        return StatusResult.INFEASIBLE;
                    }
                }

                return StatusResult.UNBOUNDED;
            }

            currentIteration++;
            iterations.add(getIteration(problem, "Iteração " + currentIteration));
            problem.getTableau().pivot();
        }

        throw new SimplexConvergenceException(iterations);
    }

    /**
     * Encontra a coluna pivô no Tableau.
     * <p>
     * A coluna pivô é selecionada com base no custo reduzido na linha {@code Z}:
     * na forma padrão de maximização, um coeficiente estritamente negativo na linha
     * {@code Z} (respeitando a tolerância {@code EPSILON}) indica que aumentar o
     * valor dessa variável melhora o valor da função objetivo.
     * <p>
     * Se nenhuma coluna atender ao critério de negatividade, o método retorna um
     * valor sentinela (indicando que a condição de otimalidade foi alcançada).
     * 
     * @param tableau tabela Simplex onde será encontrada a coluna pivô.
     * @return o índice da coluna pivô selecionada, ou um valor sentinela se o ótimo
     *         foi atingido.
     */
    protected BigMCoefficient findPivotColumn(Tableau tableau) {
        BigMCoefficient lowestCoefficient = new BigMCoefficient(Double.MAX_VALUE, Double.MAX_VALUE);
        for (int i = 1; i < tableau.getRowZ().length - 1; i++) {
            BigMCoefficient coefficient = tableau.getRowZ()[i];
            if (coefficient.isStrictlyNegative()) {
                if (lowestCoefficient.compareTo(coefficient) > 0) {
                    lowestCoefficient = coefficient;
                    tableau.setPivotColumn(i);
                }
            }
        }

        return lowestCoefficient;
    }

    /**
     * Encontra a linha pivô no Tableau utilizando o Teste da Razão Mínima.
     * <p>
     * O método calcula a razão entre o valor do lado direito ({@code RHS}) e o
     * coeficiente da coluna pivô para cada linha.
     * <ul>
     * <li>Apenas linhas com coeficientes estritamente positivos na coluna pivô são
     * elegíveis.</li>
     * <li>A linha com a menor razão positiva é selecionada para garantir a
     * viabilidade da solução.</li>
     * <li>Se nenhuma linha for elegível (nenhum coeficiente positivo na coluna
     * pivô), o método define o índice da linha como {@code -1}, indicando que o
     * problema é ilimitado.</li>
     * </ul>
     * 
     * @param tableau tabela Simplex onde será encontrada a linha pivô.
     */
    protected void findPivotRow(Tableau tableau) {
        int pivotColumn = tableau.getPivotColumn();
        int chosenRow = -1;
        double lowest = Double.MAX_VALUE;

        for (int i = 0; i < tableau.getMatrix().length; i++) {
            double columnValue = tableau.getMatrix()[i][pivotColumn];
            if (columnValue > MathHelper.EPSILON) {
                double rightHandValue = tableau.getMatrix()[i][tableau.getMatrix()[i].length - 1];
                if (Math.abs(rightHandValue) < MathHelper.EPSILON) {
                    rightHandValue = 0.0;
                }

                double div = rightHandValue / columnValue;
                if (div < lowest) {
                    lowest = div;
                    chosenRow = i;
                }
            }
        }

        tableau.setPivotRow(chosenRow);
    }

    /**
     * Retorna uma iteração do processo de Simplex.
     * <p>
     * A iteração inclui o título da iteração, os índices da coluna e linha pivô, os
     * nomes das linhas e colunas, e a matriz do tableau.
     * </p>
     * 
     * @param problem problema de Simplex para o qual a iteração será retornada.
     * @param title   título da iteração ("Inicial", "Iteração", "Final").
     * @return uma instância de {@link Iteration} representando a iteração atual do
     *         processo de Simplex.
     */
    private Iteration getIteration(SimplexProblem problem, String title) {
        Tableau tableau = problem.getTableau();

        String[] rowNames = new String[tableau.getMatrix().length + 1];
        String[][] matrix = new String[tableau.getMatrix().length + 1][tableau.getMatrix()[0].length];

        for (int i = 0; i < tableau.getRowZ().length; i++) {
            matrix[0][i] = tableau.getRowZ()[i].toString();
        }

        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i + 1][j] = (MathHelper.formatDouble(tableau.getMatrix()[i][j]));
            }
        }

        rowNames[0] = tableau.getColumnNames()[0];
        for (int i = 0; i < problem.getConstraints().size(); i++) {
            rowNames[i + 1] = tableau.getColumnNames()[tableau.getBasicIndexes()[i]];
        }

        return new Iteration(
                title,
                title.equals("Final") ? new int[] { -1, -1 }
                        : new int[] { tableau.getPivotColumn(), tableau.getPivotRow() },
                rowNames,
                tableau.getColumnNames(),
                matrix);
    }

    /**
     * Retorna o resultado do processo de Simplex.
     * <p>
     * O resultado inclui o status do resultado, o valor da função objetivo,
     * e os valores das variáveis de decisão. Se o problema for ilimitado, o valor
     * da função objetivo será {@code NA}. Se o problema for inviável, o valor da
     * função
     * objetivo será {@code 0}.
     * </p>
     * 
     * @param problem    problema de Simplex para o qual o resultado será retornado.
     * @param iterations lista de iterações do processo de Simplex.
     * @param status     status do resultado do processo de Simplex.
     * @return uma instância de {@link SimplexResult} representando os dados finais
     *         do
     *         processo de Simplex.
     */
    private SimplexResult getResult(SimplexProblem problem, List<Iteration> iterations, StatusResult status) {
        Tableau tableau = problem.getTableau();
        final int rightHandColumn = tableau.getColumnNames().length - 1;
        Map<String, String> variableValues = getVariableValues(tableau);

        if (status == StatusResult.UNBOUNDED) {
            return new SimplexResult(iterations, status.getTitle(), problem.getOptimizationType().getTitle(), "NA",
                    variableValues);
        }

        for (int i = 0; i < tableau.getBasicIndexes().length; i++) {
            String variableName = tableau.getColumnNames()[tableau.getBasicIndexes()[i]];

            if (variableName.startsWith("a") && tableau.getMatrix()[i][rightHandColumn] > MathHelper.EPSILON) {
                return new SimplexResult(iterations, StatusResult.INFEASIBLE.getTitle(),
                        problem.getOptimizationType().getTitle(), MathHelper.formatDouble(0), variableValues);
            }
        }

        for (int j = 1; j < rightHandColumn; j++) {
            if (tableau.getColumnNames()[j].startsWith("x") && !tableau.isBasic(j)) {
                BigMCoefficient coefficient = tableau.getRowZ()[j];
                if (Math.abs(coefficient.bigM()) < MathHelper.EPSILON
                        && Math.abs(coefficient.value()) < MathHelper.EPSILON) {
                    status = StatusResult.MULTIPLE_OPTIMAL;
                    break;
                }
            }
        }

        double objectiveValue = problem.getOptimizationType() == OptimizationType.MAXIMIZATION
                ? tableau.getRowZ()[rightHandColumn].value()
                : tableau.getRowZ()[rightHandColumn].value() * (-1);

        return new SimplexResult(
                iterations,
                status.getTitle(),
                problem.getOptimizationType().getTitle(),
                MathHelper.formatDouble(objectiveValue),
                variableValues);
    }

    /**
     * Retorna os valores das variáveis de decisão {@code x}, de folga {@code s} e
     * artificiais {@code a} a partir da tabela Simplex.
     * 
     * @param tableau tabela Simplex contendo os dados do problema.
     * @return um mapa com os nomes e valores das variáveis de decisão.
     */
    private @NonNull Map<String, String> getVariableValues(Tableau tableau) {
        Map<String, String> variableValues = new TreeMap<>((v1, v2) -> {
            String prefix1 = v1.replaceAll("\\d+", "");
            String prefix2 = v2.replaceAll("\\d+", "");

            if (!prefix1.equals(prefix2)) {
                return prefix1.compareTo(prefix2);
            }

            try {
                int num1 = Integer.parseInt(v1.replaceAll("\\D+", ""));
                int num2 = Integer.parseInt(v2.replaceAll("\\D+", ""));
                return Integer.compare(num1, num2);
            } catch (NumberFormatException e) {
                return v1.compareTo(v2);
            }
        });

        for (int j = 1; j < tableau.getColumnNames().length - 1; j++) {
            variableValues.put(tableau.getColumnNames()[j], "0");
        }

        for (int i = 0; i < tableau.getBasicIndexes().length; i++) {
            int basicColumn = tableau.getBasicIndexes()[i];
            variableValues.put(tableau.getColumnNames()[basicColumn],
                    MathHelper.formatDouble(tableau.getMatrix()[i][tableau.getColumnNames().length - 1]));
        }

        Map<String, String> sortedVariables = new LinkedHashMap<>();

        variableValues.forEach((key, value) -> {
            if (key.startsWith("x"))
                sortedVariables.put(key, value);
        });
        variableValues.forEach((key, value) -> {
            if (key.startsWith("s"))
                sortedVariables.put(key, value);
        });
        variableValues.forEach((key, value) -> {
            if (key.startsWith("a"))
                sortedVariables.put(key, value);
        });

        return sortedVariables;
    }
}
