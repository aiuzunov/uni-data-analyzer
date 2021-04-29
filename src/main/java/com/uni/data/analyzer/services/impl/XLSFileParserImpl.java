package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.services.FileParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class XLSFileParserImpl extends AbstractFileParser {

    @Override
    public void parseFile(MultipartFile file) {
        //TODO: implement
    }

    @Override
    public boolean validate(InputStream fileInputStream) throws IOException, InvalidFormatException {
        OPCPackage pkg = OPCPackage.open(fileInputStream);
        var workbook = new XSSFWorkbook(pkg);
        var sheet = workbook.sheetIterator().next();
        var cells = sheet.iterator().next();
        List<String> headers = new ArrayList<>();
        for(var cell: cells) {
            headers.add(cell.getStringCellValue());
        }
        return validateHeadings(headers);
    }

    @Override
    public boolean canHandleFile(String filename) {
        return filename.endsWith(".xls") || filename.endsWith(".xlsx");
    }
}
