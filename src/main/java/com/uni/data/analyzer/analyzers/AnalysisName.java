package com.uni.data.analyzer.analyzers;


import com.uni.data.analyzer.exceptions.AnalysisNameNotFoundException;

import static utils.ArgumentValidationUtils.requireNonNull;

public enum AnalysisName {

    CORRELATION("correlation", "корелационен анализ"),
    CENTRAL_TREND("central-trend", "анализ за централна тенденция"),
    DISPERSION("dispersion", "анализ за мерки за резсейване"),
    FREQUENCY("frequency", "честотен анализ");

    private static final String UNSUPPORTED_ANALYSIS_MESSAGE = "Анализ '%s' не е поддържан";
    private final String name;
    private final String bulgarianName;

    AnalysisName(String name, String bulgarianName) {
        this.name = name;
        this.bulgarianName = bulgarianName;
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

    public String getBulgarianName() {
        return bulgarianName;
    }
}
