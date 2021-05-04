package com.uni.data.analyzer.services;

import com.uni.data.analyzer.FileData;
import com.uni.data.analyzer.data.model.UploadedFile;
import com.uni.data.analyzer.exceptions.IllegalFormatException;
import com.uni.data.analyzer.exceptions.ParseException;
import org.yaml.snakeyaml.parser.ParserException;

import java.util.List;
import java.util.Set;

public interface FileDataService {

    default FileData<String> parseFile(UploadedFile file) {
        try {
            return parseFileInternal(file);
        } catch (ParserException | IllegalFormatException ex) {
            return new FileData<>(ex.getMessage());
        }
    }

    default FileData<Double> parseFileNumeric(UploadedFile file) {
        try {
            return parseFileNumericInternal(file);
        } catch (ParseException | IllegalFormatException ex) {
            return new FileData<>(ex.getMessage());
        }
    }

    UploadedFile getRequiredLogFile(Set<UploadedFile> files);

    List<UploadedFile> getRequiredResultsFiles(Set<UploadedFile> files);

    FileData<String> parseFileInternal(UploadedFile file);

    FileData<Double> parseFileNumericInternal(UploadedFile file);

}
