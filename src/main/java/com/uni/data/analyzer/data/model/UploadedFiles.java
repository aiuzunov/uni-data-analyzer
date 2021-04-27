package com.uni.data.analyzer.data.model;

import javax.persistence.*;

@Entity(name="UploadedFiles")
public class UploadedFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private AnalysisOperation analysisOperation;
}
