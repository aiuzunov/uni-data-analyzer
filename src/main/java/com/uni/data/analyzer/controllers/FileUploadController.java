package com.uni.data.analyzer.controllers;

import com.opencsv.exceptions.CsvValidationException;
import com.uni.data.analyzer.services.FileStorageService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/analyse")
public class FileUploadController {

    @Autowired
    private FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity uploadFileForAnalysis(@RequestParam("file") MultipartFile file) {
        try {
            storageService.storeFile(file, RequestContextHolder.getRequestAttributes().getSessionId());
        } catch (IOException | NotOfficeXmlFileException | InvalidFormatException | CsvValidationException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload/files")
    public ResponseEntity uploadFilesForAnalysis(@RequestParam("files") MultipartFile[] files) {
        try {
            storageService.storeFiles(files, RequestContextHolder.getRequestAttributes().getSessionId());
        } catch (IOException | NotOfficeXmlFileException | InvalidFormatException | CsvValidationException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok().build();
    }
}
