package com.uni.data.analyzer.analyzers;

import com.uni.data.analyzer.analyzers.analysis.FrequencyAnalysis;

import java.util.Map;

public interface FrequencyAnalyzer {

    FrequencyAnalysis analyze(Map<String, Integer> values);

}