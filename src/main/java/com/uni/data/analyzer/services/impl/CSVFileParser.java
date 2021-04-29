package com.uni.data.analyzer.services.impl;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.uni.data.analyzer.services.FileParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URLConnection;
import java.util.Arrays;

@Service
public class CSVFileParser extends AbstractFileParser {
    @Override
    public void parseFile(MultipartFile file) throws IOException {
        // TODO: Implement
    }

    @Override
    public boolean validate(InputStream fileInputStream) throws IOException, InvalidFormatException, CsvValidationException {
        CSVReader  reader = new CSVReaderBuilder(new InputStreamReader(fileInputStream))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();
        String[] headerLine = reader.readNext();
        return validateHeadings(Arrays.asList(headerLine));
    }

    @Override
    public boolean canHandleFile(String filename) {
        return filename.endsWith(".csv");
    }
}
