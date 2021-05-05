package com.uni.data.analyzer.services;

import java.io.IOException;
import java.util.Map;

public interface FileDownloadService {
    double[][] parseJSON(Map<String, Object> json) throws IOException;

    byte[] GenerateFile() throws IOException;
}
