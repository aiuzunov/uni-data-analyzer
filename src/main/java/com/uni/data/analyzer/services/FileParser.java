package com.uni.data.analyzer.services;

import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileParser {

    void parseFile(MultipartFile file) throws IOException;

    boolean validate(InputStream fileInputStream) throws IOException, InvalidFormatException, CsvValidationException;

    boolean canHandleFile(String filename);
}
