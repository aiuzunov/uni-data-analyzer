package com.uni.data.analyzer.analyzers.impl;

import com.uni.data.analyzer.analyzers.DispersionAnalyzer;
import com.uni.data.analyzer.analyzers.analysis.DispersionAnalysis;

import java.util.Collection;
import java.util.Collections;

import static com.uni.data.analyzer.analyzers.AnalysisName.DISPERSION;
import static java.lang.Math.sqrt;
import static utils.AnalysisValidationUtils.requireNonEmpty;

public class DispersionAnalyzerImpl implements DispersionAnalyzer {

    @Override
    public DispersionAnalysis analyze(Collection<Integer> values) {
        requireNonEmpty(values, DISPERSION.getName());

        double scope = calculateScope(values);
        double dispersion = calculateDispersion(values);
        double standardDeviation = sqrt(dispersion);

        return new DispersionAnalysis(scope, dispersion, standardDeviation);
    }

    private double calculateScope(Collection<Integer> values) {
        return Collections.max(values) - Collections.min(values);
    }

    private double calculateDispersion(Collection<Integer> values) {
        double average = values.stream().mapToDouble(val -> val).average().orElse(0);

        return values.stream()
                .mapToDouble(value -> Math.pow(value - average, 2))
                .sum() / values.size() - 1;
    }
}