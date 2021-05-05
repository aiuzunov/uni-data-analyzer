package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.exceptions.IllegalFormatException;
import com.uni.data.analyzer.exceptions.ParseException;
import file.definitions.LogFileColumns;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

@Service
public class XLSFileParserImpl extends AbstractFileParser {

    @Override
    public boolean isLogsFile(byte[] data) {
        Row firstRow = getFirstSheet(data).iterator().next();
        Set<String> titles = parseColumnTitles(firstRow);

        return Arrays.stream(LogFileColumns.values()).allMatch(title -> titles.contains(title.getName()));
    }

    @Override
    public Map<String, List<String>> parseFile(byte[] data) {
        Sheet firstSheet = getFirstSheet(data);
        var firstRow = firstSheet.iterator().next();

        Map<String, List<String>> parsedData = new HashMap<>();
        Map<Integer, String> columnTitles = new HashMap<>();

        parseColumnTitles(firstRow, parsedData, columnTitles);
        if (columnTitles.size() == 0) {
            return Map.of();
        }

        Iterator<Row> rowIterator = getRowIterator(firstSheet);
        return parseRowStringData(rowIterator, parsedData, columnTitles, this::getCellStringValue);
    }

    @Override
    public Map<String, List<Double>> parseFileNumeric(byte[] data) {
        Sheet firstSheet = getFirstSheet(data);
        var firstRow = firstSheet.iterator().next();

        Map<String, List<Double>> parsedData = new HashMap<>();
        Map<Integer, String> columnTitles = new HashMap<>();

        parseColumnTitles(firstRow, parsedData, columnTitles);
        if (columnTitles.size() == 0) {
            return Map.of();
        }

        Iterator<Row> rowIterator = getRowIterator(firstSheet);
        return parseRowStringData(rowIterator, parsedData, columnTitles, Cell::getNumericCellValue);
    }

    private Sheet getFirstSheet(byte[] data) {
        try {
            OPCPackage pkg = OPCPackage.open(new ByteArrayInputStream(data));
            var workbook = new XSSFWorkbook(pkg);
            return workbook.sheetIterator().next();
        } catch (IOException ex) {
            throw new ParseException("Възникна грешка при четенето на файл", ex);
        } catch (InvalidFormatException ex) {
            throw new IllegalFormatException("Невалиден формат на файл", ex);
        }
    }

    private String getCellStringValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
            case BLANK:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            case _NONE:
            default:
                throw new ParseException("Съществуа клетка без тип във файла");
        }
    }

    private Set<String> parseColumnTitles(Row row) {
        Set<String> titles = new HashSet<>();

        for (Cell cell : row) {
            titles.add(getCellStringValue(cell));
        }

        return titles;
    }

    private <T> void parseColumnTitles(Row row, Map<String, List<T>> parsedData, Map<Integer, String> columnTitles) {
        int i = 0;
        for (Cell cell : row) {
            columnTitles.put(i++, cell.getStringCellValue());
            parsedData.put(cell.getStringCellValue(), new ArrayList<>());
        }
    }

    private <T> Map<String, List<T>> parseRowStringData(Iterator<Row> rowIterator,
                                                        Map<String, List<T>> parsedData,
                                                        Map<Integer, String> columnTitles,
                                                        Function<Cell, T> getCellDataFunc) {
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            int i = 0;
            for (var cell : row) {
                parsedData.get(columnTitles.get(i++)).add(getCellDataFunc.apply(cell));
            }
        }

        return parsedData;
    }

    private Iterator<Row> getRowIterator(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        return rowIterator;
    }

    @Override
    public boolean validate(InputStream fileInputStream) throws IOException, InvalidFormatException {
        OPCPackage pkg = OPCPackage.open(fileInputStream);
        var workbook = new XSSFWorkbook(pkg);
        var sheet = workbook.sheetIterator().next();
        var cells = sheet.iterator().next();
        List<String> headers = new ArrayList<>();
        for (var cell : cells) {
            headers.add(cell.getStringCellValue());
        }
        return validateHeadings(headers);
    }

    @Override
    public boolean canHandleFile(String filename) {
        return filename.endsWith(".xls") || filename.endsWith(".xlsx");
    }
}
