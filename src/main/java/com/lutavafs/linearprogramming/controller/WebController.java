package com.lutavafs.linearprogramming.controller;

import com.lutavafs.linearprogramming.domain.simplex.model.SimplexProblem;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.solver.impl.SimplexSolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class WebController {

    private final SimplexSolver simplexSolver;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/simplex")
    public String simplex(Model model) {
        model.addAttribute("simplexProblem", new SimplexProblem());
        model.addAttribute("simplexResult", null);
        return "simplex";
    }

    @PostMapping("/simplex/calculate")
    public String simplexCalculate(@ModelAttribute("simplexProblem") SimplexProblem problem, Model model) {
        SimplexResult result = simplexSolver.calculate(problem);

        model.addAttribute("simplexResult", result);
        return "simplex-result";
    }
}
