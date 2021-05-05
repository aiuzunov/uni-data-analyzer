package com.uni.data.analyzer.analyzers.impl;

import com.uni.data.analyzer.analyzers.FrequencyAnalyzer;
import com.uni.data.analyzer.analyzers.analysis.FrequencyAnalysis;
import com.uni.data.analyzer.analyzers.analysis.FrequencyAnalysisEntry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.uni.data.analyzer.analyzers.AnalysisName.FREQUENCY;
import static utils.AnalysisValidationUtils.requireNonEmpty;

public class FrequencyAnalyzerImpl implements FrequencyAnalyzer {

    @Override
    public FrequencyAnalysis analyze(Map<String, Integer> values) {
        requireNonEmpty(values, FREQUENCY.getName());

        int numberOfResults = values.values().stream().reduce(0, Integer::sum);

        return numberOfResults == 0 ?
                buildResultsWithNoValues(values) :
                calculateResults(values, numberOfResults);
    }

    private FrequencyAnalysis buildResultsWithNoValues(Map<String, Integer> values) {
        List<FrequencyAnalysisEntry> entries = values.keySet().stream()
                .map(strings -> new FrequencyAnalysisEntry(strings, 0, 0))
                .collect(Collectors.toUnmodifiableList());

        return new FrequencyAnalysis(entries);
    }

    private FrequencyAnalysis calculateResults(Map<String, Integer> values, int numberOfResults) {
        List<FrequencyAnalysisEntry> entries = values.entrySet().stream().map(entry -> {
            int absoluteFrequency = entry.getValue();
            double relativeFrequency = absoluteFrequency * 1.0 / numberOfResults;

            return new FrequencyAnalysisEntry(entry.getKey(), absoluteFrequency, relativeFrequency);
        }).collect(Collectors.toUnmodifiableList());

        return new FrequencyAnalysis(entries);
    }
}