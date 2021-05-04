package com.uni.data.analyzer.services.impl;

import com.opencsv.exceptions.CsvValidationException;
import com.uni.data.analyzer.data.model.analysis.AnalysisOperation;
import com.uni.data.analyzer.data.model.UploadedFile;
import com.uni.data.analyzer.data.repositories.AnalysisOperationRepository;
import com.uni.data.analyzer.data.repositories.UploadedFilesRepository;
import com.uni.data.analyzer.services.FileParser;
import com.uni.data.analyzer.services.FileStorageService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private UploadedFilesRepository uploadedFilesRepository;

    @Autowired
    private AnalysisOperationRepository analysisOperationRepository;

    @Autowired
    private List<FileParser> parsers;

    @Override
    public void storeFile(MultipartFile file, String sessionId) throws IOException, InvalidFormatException, CsvValidationException {
        var zipStream = new ZipInputStream(file.getInputStream());
        boolean hasEntries = false;

        for (ZipEntry entry; (entry = zipStream.getNextEntry()) != null; ) {
            hasEntries = true;
            if(entry.isDirectory()) {
               throw new InvalidFormatException("It is expected to have flat structure within the Zip file");
            }
            String entryName = entry.getName();
            var parser = parsers.stream()
                    .filter(p -> p.canHandleFile(entryName))
                    .findFirst()
                    .orElseThrow(() -> new InvalidFormatException("Format is not supported"));
            if(!parser.validate(zipStream)) {
                throw new InvalidFormatException("Failed to parse zip file");
            }
        }
        if(!hasEntries) {
            throw new InvalidFormatException("Empty zip file.");
        }


        zipStream = new ZipInputStream(file.getInputStream());
        var analysisOperation = new AnalysisOperation(sessionId);
        analysisOperation = analysisOperationRepository.save(analysisOperation);

        ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
        int bytesRead;
        byte[] tempBuffer = new byte[8192*2];
        for (ZipEntry entry; (entry = zipStream.getNextEntry()) != null; ) {
            while((bytesRead = zipStream.read()) != -1) {
                streamBuilder.write(tempBuffer, 0, bytesRead);
            }
            var fileEntity = new UploadedFile(entry.getName(), streamBuilder.toByteArray());
            uploadedFilesRepository.save(fileEntity);
            analysisOperation.getUploadedFiles().add(fileEntity);
        }
        analysisOperationRepository.save(analysisOperation);
    }

    @Override
    public void storeFiles(MultipartFile[] files, String sessionId) throws IOException, InvalidFormatException, CsvValidationException {
        for (MultipartFile file : files) {
            var parser = parsers.stream()
                    .filter(p -> p.canHandleFile(file.getOriginalFilename()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidFormatException("Format is not supported"));
            if (!parser.validate(file.getInputStream())) {
                throw new InvalidFormatException("Invalid format within the .xlsx");
            }
        }

        var analysisOperation = new AnalysisOperation(sessionId);
        analysisOperation = analysisOperationRepository.save(analysisOperation);

        for (MultipartFile file : files) {
            var fileEntity = new UploadedFile(file.getOriginalFilename(), file.getBytes());
            fileEntity.setAnalysisOperation(analysisOperation);
            uploadedFilesRepository.save(fileEntity);
            analysisOperation.getUploadedFiles().add(fileEntity);
        }
        analysisOperationRepository.save(analysisOperation);
    }
}
