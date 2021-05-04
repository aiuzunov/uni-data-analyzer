package com.uni.data.analyzer.services;

import java.util.Map;

public interface AnalyzerService {

    Map<String, Object> createAnalysis(String analysisName, String sessionId);
}
