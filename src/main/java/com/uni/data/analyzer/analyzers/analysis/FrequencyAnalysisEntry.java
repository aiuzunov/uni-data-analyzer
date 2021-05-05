package com.uni.data.analyzer.analyzers.analysis;

public class FrequencyAnalysisEntry {

    private final String name;
    private final int absoluteFrequency;
    private final double relativeFrequency;

    public FrequencyAnalysisEntry(String name, int absoluteFrequency, double relativeFrequency) {
        this.name = name;
        this.absoluteFrequency = absoluteFrequency;
        this.relativeFrequency = relativeFrequency;
    }

    public String getName() {
        return name;
    }

    public int getAbsoluteFrequency() {
        return absoluteFrequency;
    }

    public double getRelativeFrequency() {
        return relativeFrequency;
    }
}