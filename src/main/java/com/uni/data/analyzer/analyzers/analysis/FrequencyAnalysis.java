package com.uni.data.analyzer.analyzers.analysis;

import java.util.List;

public class FrequencyAnalysis {

    private final List<FrequencyAnalysisEntry> entries;

    public FrequencyAnalysis(List<FrequencyAnalysisEntry> entries) {
        this.entries = List.copyOf(entries);
    }

    public List<FrequencyAnalysisEntry> getEntries() {
        return entries;
    }
}