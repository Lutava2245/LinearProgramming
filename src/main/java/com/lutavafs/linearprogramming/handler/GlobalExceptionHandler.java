package com.lutavafs.linearprogramming.handler;

import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.exception.SimplexConvergenceException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SimplexConvergenceException.class)
    public String handleSimplexConvergence(SimplexConvergenceException ex, Model model) {
        SimplexResult errorResult = new SimplexResult(
                Collections.emptyList(),
                StatusResult.DEGENERATE_CYCLING.getTitle(),
                "-",
                "NA",
                Collections.emptyMap()
        );

        model.addAttribute("simplexResult", errorResult);
        model.addAttribute("errorMessage", ex.getMessage());

        return "simplex-result";
    }
}