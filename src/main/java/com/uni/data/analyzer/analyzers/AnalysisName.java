package com.uni.data.analyzer.analyzers;


import com.uni.data.analyzer.exceptions.AnalysisNameNotFoundException;

import static utils.ArgumentValidationUtils.requireNonNull;

public enum AnalysisName {

    CORRELATION("correlation"),
    CENTRAL_TREND("central-trend"),
    DISPERSION("dispersion"),
    FREQUENCY("frequency");

    private static final String UNSUPPORTED_ANALYSIS_MESSAGE = "Анализ '%s' не е поддържан";
    private final String name;

    AnalysisName(String name) {
        this.name = name;
    }

    public static AnalysisName map(String name) {
        requireNonNull(name, "name");

        try {
            for (AnalysisName value : values()) {
                if (value.getName().equals(name)) {
                    return value;
                }
            }

            throw new AnalysisNameNotFoundException(String.format(UNSUPPORTED_ANALYSIS_MESSAGE, name));
        } catch (IllegalArgumentException ex) {
            throw new AnalysisNameNotFoundException(String.format(UNSUPPORTED_ANALYSIS_MESSAGE, name));
        }
    }

    public String getName() {
        return name;
    }
}
