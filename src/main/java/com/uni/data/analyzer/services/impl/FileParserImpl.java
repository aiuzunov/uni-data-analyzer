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
public class FileParserImpl implements FileParser {

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

    private boolean validateHeadings(List<String> headers) {
        if (headers.size() == 2) {
            return headers.get(0).equals("ID") && headers.get(1).equals("Result");
        }

        if (headers.size() == 5) {
            return headers.get(0).equals("Time") && headers.get(1).equals("Event context")
                    && headers.get(2).equals("Component") && headers.get(3).equals("Event name")
                    && headers.get(4).equals("Description");
        }

        return false;
    }
}
