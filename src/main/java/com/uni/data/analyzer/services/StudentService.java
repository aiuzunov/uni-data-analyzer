package com.uni.data.analyzer.services;

import com.uni.data.analyzer.FileData;

import java.util.Map;

public interface StudentService {

    Map<String, Double> getStudentGrades(FileData<Double> fileData);

}
