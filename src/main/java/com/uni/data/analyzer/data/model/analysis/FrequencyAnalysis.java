package com.uni.data.analyzer.data.model.analysis;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.uni.data.analyzer.constants.AnalysisConstants.ENTRIES;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "frequency_analysis")
public class FrequencyAnalysis extends Analysis {

    @OneToMany(cascade = {PERSIST, REMOVE})
    private List<FrequencyAnalysisEntry> entries;
    @Transient
    private Map<String, Object> values;

    protected FrequencyAnalysis() {
        values = new HashMap<>();
    }

    public FrequencyAnalysis(List<FrequencyAnalysisEntry> entries) {
        this.entries = entries;
        values = buildValues();
    }

    public FrequencyAnalysis(String error) {
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

    public List<FrequencyAnalysisEntry> entries() {
        return entries;
    }

    private Map<String, Object> buildValues() {
        List<Map<String, Object>> entryValues = entries.stream().map(
                FrequencyAnalysisEntry::getValues
        ).collect(Collectors.toUnmodifiableList());

        return Map.of(ENTRIES, entryValues);
    }
}