package com.uni.data.analyzer.data.model.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "dispersion_analysis")
public class DispersionAnalysis extends Analysis {

    public static String SCOPE = "scope";
    public static String DISPERSION = "dispersion";
    public static String STANDARD_DEVIATION = "standardDeviation";

    private double scope;
    private double dispersion;
    @Column(name = "standard_deviation")
    private double standardDeviation;
    @Transient
    private Map<String, Object> values;

    protected DispersionAnalysis() {
        values = new HashMap<>();
    }

    public DispersionAnalysis(double scope, double dispersion, double standardDeviation) {
        this.scope = scope;
        this.dispersion = dispersion;
        this.standardDeviation = standardDeviation;
        values = buildValues();
    }

    public DispersionAnalysis(String error) {
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

    public double getScope() {
        return scope;
    }

    public double getDispersion() {
        return dispersion;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    private Map<String, Object> buildValues() {
        return Map.of(
                SCOPE, scope,
                DISPERSION, dispersion,
                STANDARD_DEVIATION, standardDeviation
        );
    }

}
