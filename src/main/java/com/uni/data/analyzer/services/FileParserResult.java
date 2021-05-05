package com.uni.data.analyzer.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FileParserResult<T> {

    private final Map<String, List<T>> data;
    private final String error;

    public FileParserResult(String error) {
        data = new HashMap<>();
        this.error = error;
    }

    public FileParserResult(Map<String, List<T>> data) {
        this.data = Map.copyOf(data);
        this.error = null;
    }

    public Optional<String> getError() {
        return Optional.ofNullable(error);
    }

    public Map<String, List<T>> getData() {
        return data;
    }
}