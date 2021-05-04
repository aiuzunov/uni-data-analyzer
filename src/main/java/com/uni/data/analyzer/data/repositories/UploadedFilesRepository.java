package com.uni.data.analyzer.data.repositories;

import com.uni.data.analyzer.data.model.UploadedFile;
import org.springframework.data.repository.CrudRepository;

public interface UploadedFilesRepository extends CrudRepository<UploadedFile, Long> {

    UploadedFile findAllByAnalysisOperation_Id(Long analysisId);
}
