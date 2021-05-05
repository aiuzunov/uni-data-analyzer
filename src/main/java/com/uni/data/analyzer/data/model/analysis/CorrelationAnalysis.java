package com.uni.data.analyzer.data.model.analysis;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.uni.data.analyzer.constants.AnalysisConstants.ENTRIES;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "correlation_analysis")
public class CorrelationAnalysis extends Analysis {

    @OneToMany(cascade = {PERSIST, REMOVE})
    List<CorrelationAnalysisEntry> entries;

    @Transient
    private Map<String, Object> values;

    protected CorrelationAnalysis() {
        values = new HashMap<>();
    }

    public CorrelationAnalysis(List<CorrelationAnalysisEntry> entries) {
        this.entries = entries;
        values = buildValues();
    }

    public CorrelationAnalysis(String error) {
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

    public List<CorrelationAnalysisEntry> getEntries() {
        return entries;
    }

    private Map<String, Object> buildValues() {
        Stream<Map<String, Object>> entryValues = entries.stream().map(CorrelationAnalysisEntry::getValues);

        return Map.of(ENTRIES, entryValues);
    }
}