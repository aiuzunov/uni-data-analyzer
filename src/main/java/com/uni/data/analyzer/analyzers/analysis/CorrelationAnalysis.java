package com.uni.data.analyzer.analyzers.analysis;

public class CorrelationAnalysis {

    private final double correlationCoefficient;

    public CorrelationAnalysis(double correlationCoefficient) {
        this.correlationCoefficient = correlationCoefficient;
    }

    public double getCorrelationCoefficient() {
        return correlationCoefficient;
    }
}