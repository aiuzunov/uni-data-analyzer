package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.data.repositories.AnalysisOperationRepository;
import com.uni.data.analyzer.data.repositories.UploadedFilesRepository;
import com.uni.data.analyzer.services.FileParser;
import com.uni.data.analyzer.services.FileStorageService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.xmlbeans.impl.store.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public void storeFile(MultipartFile file) throws IOException, InvalidFormatException {
        var zipStream = new ZipInputStream(file.getInputStream());
        var tempUnzippedDir = Files.createTempDirectory("unzippedFiles");
        var tempPath = tempUnzippedDir.toAbsolutePath();
        for (ZipEntry entry; (entry = zipStream.getNextEntry()) != null; ) {
            var path = tempPath.resolve(entry.getName());
            if(entry.isDirectory()) {
               throw new InvalidFormatException("It is expected to have flat structure within the Zip file");
            }
            parser.validate(zipStream);
            Files.copy(zipStream, path);
        }
        // TODO: Store them :D
    }

    @Override
    public void storeFiles(MultipartFile[] files) throws IOException, InvalidFormatException {
        for(int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if(!parser.validate(file.getInputStream())) {
                throw new InvalidFormatException("Invalid format within the .xlsx");
            }
        }
        //TODO now store them :D
    }

}
