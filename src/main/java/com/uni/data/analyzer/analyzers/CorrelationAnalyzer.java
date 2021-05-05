package com.uni.data.analyzer.analyzers;

import com.uni.data.analyzer.analyzers.analysis.CorrelationAnalysis;
import utils.data.MultiValueMap;

public interface CorrelationAnalyzer {

    CorrelationAnalysis analyze(MultiValueMap<String, Double, Double> values);

}