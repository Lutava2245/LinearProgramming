/**
 * Fornece o motor de cálculo e as estruturas de dados para a resolução
 * de problemas de Programação Linear utilizando o Método Simplex (Big-M).
 * <p>
 * Esta camada é independente de framework web e encapsula toda a lógica
 * matemática do sistema. A arquitetura interna é dividida entre:
 * <ul>
 * <li><b>{@link com.lutavafs.linearprogramming.domain.simplex.model.Tableau}:</b>
 * Representação matricial das equações e operações de pivoteamento (Eliminação
 * de Gauss-Jordan).</li>
 * <li><b>{@link com.lutavafs.linearprogramming.domain.simplex.model.BigMCoefficient}:</b>
 * Suporte aritmético para o método Big-M utilizando comparação lexicográfica
 * (pares valor real e multiplicador de M).</li>
 * <li><b>{@link com.lutavafs.linearprogramming.solver.impl.SimplexSolver}:</b>
 * O orquestrador iterativo que controla o fluxo de resolução, testes de razão e
 * verificação de convergência.</li>
 * </ul>
 * <p>
 * Para uma explicação matemática detalhada da teoria por trás destas classes,
 * consulte o guia teórico do projeto em {@code docs/simplex.md}.
 *
 * @see com.lutavafs.linearprogramming.solver.impl.SimplexSolver
 * @see com.lutavafs.linearprogramming.domain.simplex.model.Tableau
 * @since 1.0
 */
package com.lutavafs.linearprogramming.domain.simplex;