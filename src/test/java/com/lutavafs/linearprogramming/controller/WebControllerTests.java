package com.lutavafs.linearprogramming.controller;

import com.lutavafs.linearprogramming.domain.simplex.model.SimplexProblem;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.exception.SimplexConvergenceException;
import com.lutavafs.linearprogramming.solver.impl.SimplexSolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.lutavafs.linearprogramming.util.SimplexTestFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes do controller MVC da aplicação utilizando {@link MockMvc}.
 */
@WebMvcTest(WebController.class)
public class WebControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SimplexSolver solver;

    @Test
    @DisplayName("Deve renderizar o resultado correto quando receber um problema de Simplex")
    void simplexSuccessTest() throws Exception {
        SimplexProblem problem = getMaxExample();
        SimplexResult result = getMaxExampleResult();

        when(solver.calculate(argThat(p -> p.getOptimizationType() == problem.getOptimizationType())))
                .thenReturn(result);

        mockMvc.perform(post("/simplex/calculate")
                .flashAttr("simplexProblem", problem))
                .andExpect(status().isOk())
                .andExpect(view().name("simplex-result"))
                .andExpect(model().attributeExists("simplexResult"))
                .andExpect(model().attribute("simplexResult", result))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Solução ótima")));
    }

    @Test
    @DisplayName("Deve renderizar o erro de convergência quando receber um problema de ciclo de degeneração do Simplex")
    void simplexConvergenceExceptionTest() throws Exception {
        SimplexProblem problem = getDegenerateCyclingExample();

        when(solver.calculate(any(SimplexProblem.class)))
                .thenThrow(new SimplexConvergenceException());

        mockMvc.perform(post("/simplex/calculate")
                .flashAttr("simplexProblem", problem))
                .andExpect(status().isOk())
                .andExpect(view().name("simplex-result"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    @DisplayName("Deve renderizar o formulário do Simplex quando acessar a rota")
    void simplexFormRenderTest() throws Exception {
        mockMvc.perform(get("/simplex"))
                .andExpect(status().isOk())
                .andExpect(view().name("simplex"))
                .andExpect(model().attributeExists("simplexProblem"));
    }

    @Test
    @DisplayName("Deve fazer corretamente o Data Binding e renderizar a paǵina quando receber dados do formulário")
    void dataBindingTest() throws Exception {
        SimplexResult result = getMaxExampleResult();

        when(solver.calculate(any(SimplexProblem.class))).thenReturn(result);

        mockMvc.perform(post("/simplex/calculate")
                .param("optimizationType", "MAXIMIZATION")
                .param("objectiveFunction[0]", "3.0")
                .param("objectiveFunction[1]", "5.0")
                .param("constraints[0].coefficients[0]", "1.0")
                .param("constraints[0].coefficients[1]", "2.0")
                .param("constraints[0].constraintType", "LESS_EQUAL")
                .param("constraints[0].rightHandValue", "8.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("simplex-result"))
                .andExpect(model().attributeExists("simplexResult"))
                .andExpect(model().attribute("simplexResult", result));
    }
}
