package com.uni.data.analyzer.data.repositories;

import com.uni.data.analyzer.data.model.AnalysisOperation;
import org.springframework.data.repository.CrudRepository;

public interface AnalysisOperationRepository extends CrudRepository<AnalysisOperation, String> {

    AnalysisOperation findBySessionId(String sessionId);
}
