package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.data.model.AnalysisOperation;
import com.uni.data.analyzer.data.model.UploadedFiles;
import com.uni.data.analyzer.data.repositories.AnalysisOperationRepository;
import com.uni.data.analyzer.data.repositories.UploadedFilesRepository;
import com.uni.data.analyzer.services.FileParser;
import com.uni.data.analyzer.services.FileStorageService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.xmlbeans.impl.store.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private UploadedFilesRepository uploadedFilesRepository;

    @Autowired
    private AnalysisOperationRepository analysisOperationRepository;

    @Autowired
    private FileParser parser;

    @Override
    public void storeFile(MultipartFile file, String sessionId) throws IOException, InvalidFormatException {
        var zipStream = new ZipInputStream(file.getInputStream());
        for (ZipEntry entry; (entry = zipStream.getNextEntry()) != null; ) {
            if(entry.isDirectory()) {
               throw new InvalidFormatException("It is expected to have flat structure within the Zip file");
            }
            parser.validate(zipStream);
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
            var fileEntity = new UploadedFiles(entry.getName(), streamBuilder.toByteArray());
            uploadedFilesRepository.save(fileEntity);
            analysisOperation.getUploadedFiles().add(fileEntity);
        }
        analysisOperationRepository.save(analysisOperation);
    }

    @Override
    public void storeFiles(MultipartFile[] files, String sessionId) throws IOException, InvalidFormatException {
        for(int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if(!parser.validate(file.getInputStream())) {
                throw new InvalidFormatException("Invalid format within the .xlsx");
            }
        }

        var analysisOperation = new AnalysisOperation(sessionId);
        analysisOperation = analysisOperationRepository.save(analysisOperation);

        for(int i = 0; i < files.length; i++) {
            var fileEntity = new UploadedFiles(files[i].getName(), files[i].getBytes());
            fileEntity.setAnalysisOperation(analysisOperation);
            uploadedFilesRepository.save(fileEntity);
            analysisOperation.getUploadedFiles().add(fileEntity);
        }
        analysisOperationRepository.save(analysisOperation);
    }
}
