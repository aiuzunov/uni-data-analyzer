package com.uni.data.analyzer.data.mapper;

import com.uni.data.analyzer.data.model.analysis.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalysisMapper {

    public static CorrelationAnalysis toDomainObject(Map<String, com.uni.data.analyzer.analyzers.analysis.CorrelationAnalysis> analysis) {
        List<CorrelationAnalysisEntry> entries = analysis.entrySet().stream()
                .map(entry -> new CorrelationAnalysisEntry(entry.getKey(), entry.getValue().getCorrelationCoefficient()))
                .collect(Collectors.toUnmodifiableList());

        return new CorrelationAnalysis(entries);
    }

    public static CentralTrendAnalysis toDomainObject(com.uni.data.analyzer.analyzers.analysis.CentralTrendAnalysis analysis) {
        return new CentralTrendAnalysis(analysis.getMean(), analysis.getMedian(), analysis.getModes());
    }

    public static DispersionAnalysis toDomainObject(com.uni.data.analyzer.analyzers.analysis.DispersionAnalysis analysis) {
        return new DispersionAnalysis(analysis.getScope(), analysis.getDispersion(), analysis.getStandardDeviation());
    }

    public static FrequencyAnalysis toDomainObject(com.uni.data.analyzer.analyzers.analysis.FrequencyAnalysis analysis) {
        List<FrequencyAnalysisEntry> entries = analysis.getEntries().stream()
                .map(entry -> new FrequencyAnalysisEntry(entry.getName(), entry.getAbsoluteFrequency(), entry.getRelativeFrequency()))
                .collect(Collectors.toUnmodifiableList());

        return new FrequencyAnalysis(entries);
    }
}