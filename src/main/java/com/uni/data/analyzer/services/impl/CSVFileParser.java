package com.uni.data.analyzer.services.impl;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.uni.data.analyzer.exceptions.ParseException;
import file.definitions.LogFileColumns;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CSVFileParser extends AbstractFileParser {

    private static final String PARSING_ERROR_MESSAGE = "Възникна грешка при четенето на csv файл";
    private static final String MORE_COLUMNS_THAN_COLUMN_TITLES_MESSAGE = "Същестуват данни за недефинирани колони (без име)";
    private static final char SEPARATOR = ';';

    @Override
    public boolean isLogsFile(byte[] data) {
        try {
            Set<String> titles = Arrays.stream(buildCSVReader(data).readNext())
                    .collect(Collectors.toUnmodifiableSet());
            return Arrays.stream(LogFileColumns.values()).allMatch(title -> titles.contains(title.getName()));
        } catch (IOException | CsvValidationException e) {
            throw new ParseException(PARSING_ERROR_MESSAGE);
        }
    }

    @Override
    public Map<String, List<String>> parseFile(byte[] data) {
        CSVReader reader = buildCSVReader(data);

        String[] columnTitles;
        try {
            columnTitles = reader.readNext();
        } catch (IOException | CsvValidationException ex) {
            throw new ParseException(PARSING_ERROR_MESSAGE, ex);
        }
        if (columnTitles == null) {
            return Map.of();
        }

        return parseColumnsData(reader, columnTitles);
    }

    @Override
    public Map<String, List<Double>> parseFileNumeric(byte[] data) {
        CSVReader reader = buildCSVReader(data);

        String[] columnTitles;
        try {
            columnTitles = reader.readNext();
        } catch (IOException | CsvValidationException ex) {
            throw new ParseException(PARSING_ERROR_MESSAGE, ex);
        }
        if (columnTitles == null) {
            return Map.of();
        }

        return parseColumnNumericData(reader, columnTitles);
    }

    private CSVReader buildCSVReader(byte[] data) {
        CSVParser parser = new CSVParserBuilder().withSeparator(SEPARATOR).build();
        return new CSVReaderBuilder(new InputStreamReader(new ByteArrayInputStream(data)))
                .withCSVParser(parser)
                .build();
    }

    private Map<String, List<String>> parseColumnsData(CSVReader reader, String[] columnTitles) {
        Map<String, List<String>> parsedData = new HashMap<>();
        Arrays.stream(columnTitles).forEach(columnTitle -> parsedData.put(columnTitle, new ArrayList<>()));

        return readColumnData(reader, columnTitles, parsedData);
    }

    private Map<String, List<String>> readColumnData(CSVReader reader, String[] columnTitles, Map<String, List<String>> parsedData) {
        String[] lines;

        try {
            while ((lines = reader.readNext()) != null) {
                if (lines.length > columnTitles.length) {
                    throw new ParseException(MORE_COLUMNS_THAN_COLUMN_TITLES_MESSAGE);
                }

                for (int i = 0; i < lines.length; i++) {
                    parsedData.get(columnTitles[i]).add(lines[i]);
                }
            }

            return parsedData;
        } catch (IOException | CsvValidationException ex) {
            throw new ParseException(PARSING_ERROR_MESSAGE, ex);
        }
    }

    private Map<String, List<Double>> parseColumnNumericData(CSVReader reader, String[] columnTitles) {
        Map<String, List<Double>> parsedData = new HashMap<>();
        Arrays.stream(columnTitles).forEach(columnTitle -> parsedData.put(columnTitle, new ArrayList<>()));

        return readColumnNumericData(reader, columnTitles, parsedData);
    }

    private Map<String, List<Double>> readColumnNumericData(CSVReader reader, String[] columnTitles, Map<String, List<Double>> parsedData) {
        String[] lines;

        try {
            while ((lines = reader.readNext()) != null) {
                if (lines.length > columnTitles.length) {
                    throw new ParseException(MORE_COLUMNS_THAN_COLUMN_TITLES_MESSAGE);
                }

                for (int i = 0; i < lines.length; i++) {
                    parsedData.get(columnTitles[i]).add(Double.parseDouble(lines[i].replaceAll(",", ".")));
                }
            }

            return parsedData;
        } catch (IOException | CsvValidationException ex) {
            throw new ParseException(PARSING_ERROR_MESSAGE, ex);
        } catch (NumberFormatException ex) {
            throw new ParseException("Възникна грешка при преобразуването на данните в числа", ex);
        }
    }

    @Override
    public boolean validate(InputStream fileInputStream) throws IOException, InvalidFormatException, CsvValidationException {
        CSVReader reader = new CSVReaderBuilder(new InputStreamReader(fileInputStream))
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
