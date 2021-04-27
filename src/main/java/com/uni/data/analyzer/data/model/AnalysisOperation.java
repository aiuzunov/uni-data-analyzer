package com.uni.data.analyzer.data.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="AnalysisOperation")
public class AnalysisOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sessionId;

    @OneToMany(mappedBy = "analysisOperation")
    private Set<Results> results;

    @OneToMany(mappedBy = "analysisOperation")
    private Set<UploadedFiles> uploadedFiles;

    public AnalysisOperation(String sessionId) {
        this.sessionId = sessionId;
        this.results = new HashSet<>();
        this.uploadedFiles = new HashSet<>();
    }

    public AnalysisOperation() {}

    public Long getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Set<Results> getResults() {
        return results;
    }

    public Set<UploadedFiles> getUploadedFiles() {
        return uploadedFiles;
    }
}
