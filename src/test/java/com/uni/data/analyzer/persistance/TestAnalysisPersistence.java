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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
