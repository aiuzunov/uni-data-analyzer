package com.uni.data.analyzer.services;

import com.uni.data.analyzer.services.impl.FileStorageServiceImpl;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    void storeFile(MultipartFile file) throws IOException, InvalidFormatException;

    void storeFiles(MultipartFile[] file) throws IOException, InvalidFormatException;
}
