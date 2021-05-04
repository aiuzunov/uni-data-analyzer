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

    // определи абсолютна и относителна честота на избраните данни от (Вариант А)
    // прегледани лекции - всяка една по отделно или заедно
    // данни да се визуализират чрез честотна таблица

    //  колко пъти всяка една стойност се среща в множество от данни на извадката
    // броя на появявания на дадена стойност в това множество се нарича абсолютна честота

    // когато дадена абсолютна честота се раздели на общия брой на данните в извадката то се получава относителна честота на съответната стойност

    // get the number of results per key
    // the number of results per key is that key's absolute frequency
    // the number of results per key divided by the count of results it the key's relative frequency

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