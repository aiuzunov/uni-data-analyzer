package com.uni.data.analyzer.data.repositories;

import com.uni.data.analyzer.data.model.analysis.AnalysisOperation;
import org.springframework.data.repository.CrudRepository;

public interface AnalysisOperationRepository extends CrudRepository<AnalysisOperation, String> {

    AnalysisOperation findFirstBySessionIdOrderByIdDesc(String sessionId);
}
