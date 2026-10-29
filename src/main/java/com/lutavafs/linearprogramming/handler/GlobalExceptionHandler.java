package com.lutavafs.linearprogramming.handler;

import com.lutavafs.linearprogramming.domain.simplex.enums.StatusResult;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.exception.SimplexConvergenceException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

/**
 * Handler global para exceções da aplicação.
 * <p>
 * Esta classe é responsável por centralizar o tratamento de exceções,
 * fornecendo os recursos necessários para renderizar a paǵina sem a quebrar.
 * <p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções do tipo {@code SimplexConvergenceException}, retornando um
     * {@link SimplexResult} vazio com o status {@code DEGENERATE_CYCLING}.
     *
     * @param ex    exceção do tipo {@code SimplexConvergenceException} lançada.
     * @param model interface que contem atributos para renderizar a página.
     * @return o template HTML do resultado do simplex contendo o
     *         {@link SimplexResult}
     *         vazio com o status.
     */
    @ExceptionHandler(SimplexConvergenceException.class)
    public String handleSimplexConvergence(SimplexConvergenceException ex, Model model) {
        SimplexResult errorResult = new SimplexResult(
                Collections.emptyList(),
                StatusResult.DEGENERATE_CYCLING.getTitle(),
                "-",
                "NA",
                Collections.emptyMap());

        model.addAttribute("simplexResult", errorResult);
        model.addAttribute("errorMessage", ex.getMessage());

        return "simplex-result";
    }
}