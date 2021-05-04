package com.uni.data.analyzer.analyzers.impl;

import com.uni.data.analyzer.analyzers.CentralTrendAnalyzer;
import com.uni.data.analyzer.analyzers.analysis.CentralTrendAnalysis;

import java.util.*;
import java.util.stream.Collectors;

import static com.uni.data.analyzer.analyzers.AnalysisName.CENTRAL_TREND;
import static utils.AnalysisValidationUtils.requireNonEmpty;

public class CentralTrendAnalyzerImpl implements CentralTrendAnalyzer {

    @Override
    public CentralTrendAnalysis analyze(List<Integer> values) {
        requireNonEmpty(values, CENTRAL_TREND.getName());

        List<Integer> modes = calculateModes(values);
        double median = calculateMedian(values);
        double mean = calculateMean(values);

        return new CentralTrendAnalysis(mean, median, modes);
    }

    private double calculateMean(Collection<Integer> values) {
        return values.stream().mapToDouble(number -> number).average().orElse(0);
    }

    private double calculateMedian(List<Integer> values) {
        if (values.size() % 2 == 0) {
            int middle = values.size() / 2;

            return (values.get(middle) * 1.0 + values.get(middle - 1)) / 2;
        }

        return values.get(values.size() / 2) * 1.0 / 2;
    }

    private List<Integer> calculateModes(Collection<Integer> values) {
        Map<Integer, Integer> data = new LinkedHashMap<>();

        values.forEach(value -> {
            if (data.containsKey(value)) {
                data.put(value, data.get(value) + 1);
            } else {
                data.put(value, 1);
            }
        });

        int max = Collections.max(data.values());

        if (max == 1) {
            return List.of();
        }

        return data.entrySet().stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableList());
    }
}