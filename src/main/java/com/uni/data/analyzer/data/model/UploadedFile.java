package com.uni.data.analyzer.data.model;

import com.uni.data.analyzer.data.model.analysis.AnalysisOperation;

import javax.persistence.*;

@Entity(name = "UploadedFiles")
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    private AnalysisOperation analysisOperation;

    public UploadedFile() {
    }

    public UploadedFile(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public AnalysisOperation getAnalysisOperation() {
        return analysisOperation;
    }

    public void setAnalysisOperation(AnalysisOperation analysisOperation) {
        this.analysisOperation = analysisOperation;
    }
}
