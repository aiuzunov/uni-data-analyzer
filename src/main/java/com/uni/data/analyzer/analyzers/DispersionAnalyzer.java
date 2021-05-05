package com.uni.data.analyzer.analyzers;

import com.uni.data.analyzer.analyzers.analysis.DispersionAnalysis;

import java.util.Collection;

public interface DispersionAnalyzer {

    DispersionAnalysis analyze(Collection<Integer> values);

}