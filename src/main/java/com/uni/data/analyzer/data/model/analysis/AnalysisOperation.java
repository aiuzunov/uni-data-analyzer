package com.uni.data.analyzer.data.model.analysis;


import com.uni.data.analyzer.analyzers.AnalysisName;
import com.uni.data.analyzer.data.model.BaseEntity;
import com.uni.data.analyzer.data.model.UploadedFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity(name = "AnalysisOperation")
public class AnalysisOperation extends BaseEntity {

    @OneToMany
    private final Map<AnalysisName, Analysis> analysis;
    @Column(name = "session_id")
    private String sessionId;
    @OneToMany(mappedBy = "analysisOperation")
    private Set<UploadedFile> uploadedFiles;

    public AnalysisOperation(String sessionId) {
        this.sessionId = sessionId;
        this.analysis = new HashMap<>();
        this.uploadedFiles = new HashSet<>();
    }

    public AnalysisOperation() {
        this.analysis = new HashMap<>();
    }

    public String getSessionId() {
        return sessionId;
    }

    public Map<AnalysisName, Analysis> getAnalysis() {
        return analysis;
    }

    public Set<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }
}
