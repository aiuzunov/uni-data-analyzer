package com.uni.data.analyzer.services;

import com.uni.data.analyzer.FileData;

import java.util.List;
import java.util.Map;

public interface CourseService {

    List<String> getViewedCourses(Map<String, List<String>> logsFile);

    Map<String, Map<String, Integer>> getViewedCoursesByStudent(FileData<String> logFile);
}
