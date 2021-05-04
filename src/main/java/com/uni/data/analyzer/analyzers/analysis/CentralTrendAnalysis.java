package com.uni.data.analyzer.analyzers.analysis;

import java.util.Collection;
import java.util.List;

public class CentralTrendAnalysis {

    private final double mean;
    private final double median;
    private final Collection<Integer> modes;

    public CentralTrendAnalysis(double mean, double median, List<Integer> modes) {
        this.mean = mean;
        this.median = median;
        this.modes = List.copyOf(modes);
    }

    public double getMean() {
        return mean;
    }

    public double getMedian() {
        return median;
    }

    public Collection<Integer> getModes() {
        return modes;
    }
}