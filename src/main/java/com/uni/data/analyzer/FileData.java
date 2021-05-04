package com.uni.data.analyzer;

import com.uni.data.analyzer.data.model.UploadedFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FileData<T> {

    private final Map<String, List<T>> data;
    private final UploadedFile file;
    private final String error;

    public FileData(UploadedFile file, Map<String, List<T>> data) {
        this.file = file;
        this.data = Map.copyOf(data);
        this.error = null;
    }

    public FileData(String error) {
        this.error = error;
        file = null;
        data = new HashMap<>();
    }

    public UploadedFile getFile() {
        return file;
    }

    public Map<String, List<T>> getData() {
        return data;
    }

    public Optional<String> getError() {
        return Optional.ofNullable(error);
    }
}