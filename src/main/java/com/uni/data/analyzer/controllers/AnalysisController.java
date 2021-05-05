package com.uni.data.analyzer.controllers;

import com.uni.data.analyzer.services.AnalyzerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static utils.ArgumentValidationUtils.requireNonNull;

@RestController
public class AnalysisController {

    private final AnalyzerService analyzerService;

    public AnalysisController(AnalyzerService analyzerService) {
        this.analyzerService = requireNonNull(analyzerService, "analyzer service");
    }

    @PostMapping(value = "/analysis/{analysisName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createAnalysis(HttpSession session, @PathVariable("analysisName") String analysisName) {
        Map<String, Object> analysisValues = analyzerService.createAnalysis(analysisName, session.getId());
        return ok().body(analysisValues);
    }
}
