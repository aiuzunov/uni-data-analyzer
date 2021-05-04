package com.uni.data.analyzer.data.model.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@Entity
@Table(name = "correlation_analysis_entry")
public class CorrelationAnalysisEntry extends Analysis {

    public static final String NAME = "name";
    public static final String CORRELATION_COEFFICIENT = "correlationCoefficient";

    @Column(name = "name")
    private String name;
    @Column(name = "correlation_coefficient")
    private double correlationCoefficient;

    protected CorrelationAnalysisEntry() {

    }

    public CorrelationAnalysisEntry(String name, double correlationCoefficient) {
        this.name = name;
        this.correlationCoefficient = correlationCoefficient;
    }

    @Override
    public Map<String, Object> getValues() {
        return Map.of(
                NAME, name,
                CORRELATION_COEFFICIENT, correlationCoefficient
        );
    }

    public String getName() {
        return name;
    }

    public double getCorrelationCoefficient() {
        return correlationCoefficient;
    }
}