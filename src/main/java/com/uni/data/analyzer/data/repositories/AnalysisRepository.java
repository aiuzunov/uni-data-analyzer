package com.uni.data.analyzer.data.repositories;

import com.uni.data.analyzer.data.model.analysis.Analysis;
import com.uni.data.analyzer.data.model.UploadedFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnalysisRepository extends CrudRepository<Analysis, Long> {

    List<Analysis> findAllByFilesIn(List<UploadedFile> uploadedFiles);

}
