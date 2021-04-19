package com.uni.data.analyzer.data.repositories;

import com.uni.data.analyzer.data.model.UploadedFiles;
import org.springframework.data.repository.CrudRepository;

public interface UploadedFilesRepository extends CrudRepository<UploadedFiles, Long> {
}
