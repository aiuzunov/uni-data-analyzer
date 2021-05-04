package com.uni.data.analyzer.persistance;


import com.uni.data.analyzer.data.model.UploadedFile;
import com.uni.data.analyzer.data.model.analysis.AnalysisOperation;
import com.uni.data.analyzer.data.repositories.AnalysisOperationRepository;
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestAnalysisNamePersistence {
    @Autowired
    private AnalysisOperationRepository analysisOperationRepository;

    @Autowired
    private UploadedFilesRepository uploadedFilesRepository;

//    @Test
//    public void findBySessionId() {
//        var results = new Results();
//        var uploadedFiles = new UploadedFiles();
//        var analysisOperation = new AnalysisOperation("SessionId");
//        analysisOperation.getAnalysis().add(results);
//        analysisOperation.getUploadedFiles().add(uploadedFiles);
//
//        analysisOperationRepository.save(analysisOperation);
//
//        var operation = analysisOperationRepository.findBySessionId("SessionId");
//
//        assertThat(operation.getSessionId(), is("SessionId"));
//        assertThat(operation.getAnalysis(), hasSize(1));
//        assertThat(operation.getUploadedFiles(), hasSize(1));
//    }

    @Test
    public void fileSave() throws IOException {
        var uploadedFile = File.createTempFile("temp", null);
        FileInputStream input = new FileInputStream(uploadedFile);
        var multiPartFile = new MockMultipartFile("file", uploadedFile.getName(), "text/plain", IOUtils.toByteArray(input));

        var analysisOperation = new AnalysisOperation("sessionId");
        analysisOperation = analysisOperationRepository.save(analysisOperation);

        var entry = new UploadedFile(multiPartFile.getName(), multiPartFile.getBytes());
        entry.setAnalysisOperation(analysisOperation);
        entry = uploadedFilesRepository.save(entry);
        analysisOperation.getUploadedFiles().add(entry);

        var newEntry = uploadedFilesRepository.findAllByAnalysisOperation_Id(analysisOperation.getId());
        assertThat(newEntry.getId(), is(entry.getId()));
        assertThat(newEntry.getName(), is(multiPartFile.getName()));
    }
}
