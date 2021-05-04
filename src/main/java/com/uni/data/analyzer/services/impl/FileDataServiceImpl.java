package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.FileData;
import com.uni.data.analyzer.data.model.UploadedFile;
import com.uni.data.analyzer.exceptions.IllegalFormatException;
import com.uni.data.analyzer.exceptions.MissingRequiredDataException;
import com.uni.data.analyzer.services.FileDataService;
import com.uni.data.analyzer.services.FileParser;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileDataServiceImpl implements FileDataService {

    private static final String MISSING_REQUIRED_FILE = "Липса файл необходим за анализа: %s";

    private final List<FileParser> fileParsers;

    public FileDataServiceImpl(List<FileParser> parsers) {
        this.fileParsers = parsers;
    }

    @Override
    public UploadedFile getRequiredLogFile(Set<UploadedFile> files) {
        for (UploadedFile file : files) {
            FileParser parser = getParser(file.getName());
            if (parser.isLogsFile(file.getData())) {
                return file;
            }
        }

        throw new MissingRequiredDataException(String.format(MISSING_REQUIRED_FILE, "логове"));
    }

    @Override
    public List<UploadedFile> getRequiredResultsFiles(Set<UploadedFile> files) {
        List<UploadedFile> resultsFiles = files.stream()
                .filter(file -> !getParser(file.getName()).isLogsFile(file.getData()))
                .collect(Collectors.toUnmodifiableList());

        if (resultsFiles.isEmpty()) {
            throw new MissingRequiredDataException(String.format(MISSING_REQUIRED_FILE, "резултати"));
        }

        return resultsFiles;
    }

    @Override
    public FileData<String> parseFileInternal(UploadedFile file) {
        FileParser parser = getParser(file.getName());
        return new FileData<>(file, parser.parseFile(file.getData()));
    }

    @Override
    public FileData<Double> parseFileNumericInternal(UploadedFile file) {
        FileParser parser = getParser(file.getName());
        return new FileData<>(file, parser.parseFileNumeric(file.getData()));
    }

    private FileParser getParser(String name) {
        return fileParsers.stream()
                .filter(p -> p.canHandleFile(name))
                .findFirst().orElseThrow(() -> new IllegalFormatException(String.format("Форматът на файл: '%s', не е поддържан", name)));
    }
}