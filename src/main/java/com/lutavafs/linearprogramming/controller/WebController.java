package com.lutavafs.linearprogramming.controller;

import com.lutavafs.linearprogramming.domain.simplex.model.SimplexProblem;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.solver.Solver;
import com.lutavafs.linearprogramming.solver.impl.SimplexSolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller para gerenciar templates da landing page e do algoritmo Simplex.
 *
 * @see Solver
 */
@RequiredArgsConstructor
@Controller
public class WebController {

    private final SimplexSolver simplexSolver;

    /**
     * Exibe a landing page do projeto.
     *
     * @return o template HTML contendo landing page.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Exibe formulário para calculadora do Simplex.
     *
     * @param model interface que contem atributos para realizar cálculos do
     *              Simplex.
     * @return o template HTML contendo formulário do Simplex.
     */
    @GetMapping("/simplex")
    public String simplex(Model model) {
        model.addAttribute("simplexProblem", new SimplexProblem());
        model.addAttribute("simplexResult", null);
        return "simplex";
    }

    /**
     * Processa e exibe o resultado do cálculo do Simplex.
     *
     * @param problem um objeto contendo o problema de Simplex para calcular.
     * @param model   interface que contem atributos para realizar cálculos do
     *                Simplex.
     * @return o template HTML contendo o resultado do Simplex.
     */
    @PostMapping("/simplex/calculate")
    public String simplexCalculate(@ModelAttribute("simplexProblem") SimplexProblem problem, Model model) {
        SimplexResult result = simplexSolver.calculate(problem);

        model.addAttribute("simplexResult", result);
        return "simplex-result";
    }
}
