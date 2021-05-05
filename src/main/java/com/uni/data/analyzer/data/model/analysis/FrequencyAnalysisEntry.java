package com.uni.data.analyzer.data.model.analysis;

import com.uni.data.analyzer.data.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

import static utils.ArgumentValidationUtils.requireNonNegative;
import static utils.ArgumentValidationUtils.requireNonNull;

@Entity
@Table(name = "frequency_analysis_entry")
public class FrequencyAnalysisEntry extends BaseEntity implements AnalysisData {

    public static final String NAME = "name";
    public static final String ABSOLUTE_FREQUENCY = "absoluteFrequency";
    public static final String RELATIVE_FREQUENCY = "relativeFrequency";

    @Column(name = "name")
    private String name;
    @Column(name = "absolute_frequency")
    private int absoluteFrequency;
    @Column(name = "relative_frequency")
    private double relativeFrequency;

    protected FrequencyAnalysisEntry() {

    }

    public FrequencyAnalysisEntry(String name, int absoluteFrequency, double relativeFrequency) {
        this.name = requireNonNull(name, "name");
        this.absoluteFrequency = requireNonNegative(absoluteFrequency, "absolute frequency");
        this.relativeFrequency = requireNonNegative(relativeFrequency, "relative frequency");
    }

    @Override
    public Map<String, Object> getValues() {
        return Map.of(
                NAME, name,
                RELATIVE_FREQUENCY, relativeFrequency,
                ABSOLUTE_FREQUENCY, absoluteFrequency
        );
    }

    public double getRelativeFrequency() {
        return relativeFrequency;
    }

    public int getAbsoluteFrequency() {
        return absoluteFrequency;
    }

    public String getName() {
        return name;
    }
}