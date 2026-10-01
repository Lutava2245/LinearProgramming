package com.lutavafs.linearprogramming.controller;

import com.lutavafs.linearprogramming.domain.simplex.model.SimplexProblem;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import com.lutavafs.linearprogramming.exception.SimplexConvergenceException;
import com.lutavafs.linearprogramming.solver.impl.SimplexSolver;
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

@WebMvcTest(WebController.class)
public class WebControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SimplexSolver solver;

    @Test
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
    void simplexFormRenderTest() throws Exception {
        mockMvc.perform(get("/simplex"))
                .andExpect(status().isOk())
                .andExpect(view().name("simplex"))
                .andExpect(model().attributeExists("simplexProblem"));
    }
}
