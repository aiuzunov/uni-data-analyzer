package com.uni.data.analyzer.data.model.analysis;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "central_trend_analysis")
public class CentralTrendAnalysis extends Analysis {

    public static String MEAN = "mean";
    public static String MEDIAN = "median";
    public static String MODES = "modes";

    @Transient
    private Map<String, Object> values;
    private double mean;
    private double median;
    @ElementCollection
    private Collection<Integer> modes;

    protected CentralTrendAnalysis() {
        values = new HashMap<>();
    }

    public CentralTrendAnalysis(double mean, double median, Collection<Integer> modes) {
        this.mean = mean;
        this.median = median;
        this.modes = modes;
        values = buildValues();
    }

    public CentralTrendAnalysis(String error) {
        super(error);
    }

    @Override
    public Map<String, Object> getValues() {
        if (values.size() > 0) {
            return values;
        }

        values = buildValues();
        return values;
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

    private Map<String, Object> buildValues() {
        return Map.of(
                MEAN, mean,
                MEDIAN, median,
                MODES, modes
        );
    }
}
