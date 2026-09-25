package com.lutavafs.linearprogramming.service;

import com.lutavafs.linearprogramming.domain.simplex.model.SimplexProblem;
import com.lutavafs.linearprogramming.domain.simplex.model.SimplexResult;
import org.springframework.stereotype.Service;

@Service
public interface SimplexService {

    SimplexResult calculate(SimplexProblem problem);

}