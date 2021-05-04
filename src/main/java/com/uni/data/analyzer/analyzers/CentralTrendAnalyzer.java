package com.uni.data.analyzer.analyzers;


import com.uni.data.analyzer.analyzers.analysis.CentralTrendAnalysis;

import java.util.List;

public interface CentralTrendAnalyzer {

    CentralTrendAnalysis analyze(List<Integer> values);
}