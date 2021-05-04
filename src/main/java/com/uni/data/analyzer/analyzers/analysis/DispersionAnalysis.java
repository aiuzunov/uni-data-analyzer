package com.uni.data.analyzer.analyzers.analysis;

public class DispersionAnalysis {

    private final double scope;
    private final double dispersion;
    private final double standardDeviation;

    public DispersionAnalysis(double scope, double dispersion, double standardDeviation) {
        this.scope = scope;
        this.dispersion = dispersion;
        this.standardDeviation = standardDeviation;
    }

    public double getScope() {
        return scope;
    }

    public double getDispersion() {
        return dispersion;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }
}