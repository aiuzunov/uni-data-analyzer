package com.uni.data.analyzer.persistance;


import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.uni.data.analyzer.data.model.AnalysisOperation;
import com.uni.data.analyzer.data.model.Results;
import com.uni.data.analyzer.data.model.UploadedFiles;
import com.uni.data.analyzer.data.repositories.AnalysisOperationRepository;
import com.uni.data.analyzer.data.repositories.ResultsRepository;
import com.uni.data.analyzer.data.repositories.UploadedFilesRepository;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestAnalysisPersistence {
    @Autowired
    private AnalysisOperationRepository analysisOperationRepository;

    @Autowired
    private ResultsRepository resultsRepository;

    @Autowired
    private UploadedFilesRepository uploadedFilesRepository;

    @Test
    public void findBySessionId() {
        var results = new Results();
        var uploadedFiles = new UploadedFiles();
        var analysisOperation = new AnalysisOperation("SessionId");
        analysisOperation.getResults().add(results);
        analysisOperation.getUploadedFiles().add(uploadedFiles);

        analysisOperationRepository.save(analysisOperation);

        var operation = analysisOperationRepository.findBySessionId("SessionId");

        assertThat(operation.getSessionId(), is("SessionId"));
        assertThat(operation.getResults(), hasSize(1));
        assertThat(operation.getUploadedFiles(), hasSize(1));
    }

    @Test
    public void fileSave() throws IOException {
        var uploadedFile = File.createTempFile("temp", null);
        FileInputStream input = new FileInputStream(uploadedFile);
        var multiPartFile = new MockMultipartFile("file", uploadedFile.getName(), "text/plain", IOUtils.toByteArray(input));

        var analysisOperation = new AnalysisOperation("sessionId");
        analysisOperation = analysisOperationRepository.save(analysisOperation);

        var entry = new UploadedFiles(multiPartFile.getName(), multiPartFile.getBytes());
        entry.setAnalysisOperation(analysisOperation);
        entry = uploadedFilesRepository.save(entry);
        analysisOperation.getUploadedFiles().add(entry);

        var newEntry = uploadedFilesRepository.findAllByAnalysisOperation_Id(analysisOperation.getId());
        assertThat(newEntry.getId(), is(entry.getId()));
        assertThat(newEntry.getName(), is(multiPartFile.getName()));
    }
}
