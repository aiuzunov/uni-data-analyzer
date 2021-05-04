package com.uni.data.analyzer.services;

import com.opencsv.exceptions.CsvValidationException;
import com.uni.data.analyzer.exceptions.ParseException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface FileParser {

    boolean isLogsFile(byte[] data);

    Map<String, List<String>> parseFile(byte[] data);

    Map<String, List<Double>> parseFileNumeric(byte[] data);

    boolean validate(InputStream fileInputStream) throws IOException, InvalidFormatException, CsvValidationException;

    boolean canHandleFile(String filename);
}
