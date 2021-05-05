package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.FileData;
import com.uni.data.analyzer.exceptions.IllegalFormatException;
import com.uni.data.analyzer.services.CourseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static file.definitions.LogFileColumns.*;

public class CourseServiceImpl implements CourseService {

    private static final String STUDENT_VIEWED_COURSES_REGEX = "File: (Лекция \\d+: (.+))";
    private static final Pattern STUDENT_VIEWED_COURSES_PATTERN = Pattern.compile(STUDENT_VIEWED_COURSES_REGEX);
    private static final String DESCRIPTION_VIEWED_COURSES_REGEX = "The user with id '(\\d+)' viewed the 'resource' activity with course module id '(\\d+)'\\.";
    private static final Pattern DESCRIPTION_VIEWED_COURSES_PATTERN = Pattern.compile(DESCRIPTION_VIEWED_COURSES_REGEX);

    @Override
    public List<String> getViewedCourses(Map<String, List<String>> logsFile) {
        List<String> componentColumn = logsFile.get(COMPONENT.getName());
        List<String> eventContextColumn = logsFile.get(EVENT_CONTEXT.getName());

        List<String> viewedCourses = new ArrayList<>();

        for (int i = 0; i < componentColumn.size(); i++) {
            String component = componentColumn.get(i);
            String eventContext = eventContextColumn.get(i);

            Matcher matcher = STUDENT_VIEWED_COURSES_PATTERN.matcher(eventContext);
            if (component.equals("File") && matcher.find()) {
                viewedCourses.add(matcher.group(1));
            }
        }

        return viewedCourses;
    }

    @Override
    public Map<String, Map<String, Integer>> getViewedCoursesByStudent(FileData<String> logFile) {
        Map<String, List<String>> data = logFile.getData();


        List<String> componentColumn = data.get(COMPONENT.getName());
        List<String> eventContextColumn = data.get(EVENT_CONTEXT.getName());
        List<String> descriptionColumn = data.get(DESCRIPTION.getName());

        if (componentColumn.size() != eventContextColumn.size() || eventContextColumn.size() != descriptionColumn.size()) {
            throw new IllegalFormatException(
                    String.format("Невалиден формат на файла '%s': съществуват колони с ралични дължини",
                            logFile.getFile().getName()));
        }

        Map<String, Map<String, Integer>> viewedCoursesByStudent = new HashMap<>();

        for (int i = 0; i < componentColumn.size(); i++) {
            String component = componentColumn.get(i);
            String eventContext = eventContextColumn.get(i);
            String description = descriptionColumn.get(i);

            Matcher eventContextMatcher = STUDENT_VIEWED_COURSES_PATTERN.matcher(eventContext);
            Matcher descriptionMatcher = DESCRIPTION_VIEWED_COURSES_PATTERN.matcher(description);
            if (component.equals("File") && eventContextMatcher.find() && descriptionMatcher.find()) {
                String studentId = descriptionMatcher.group(1);
                String courseModuleId = descriptionMatcher.group(2);
                String courseName = eventContextMatcher.group(1);

                String courseUUID = String.format("%s-%s", courseModuleId, courseName);

                if (viewedCoursesByStudent.containsKey(studentId)) {
                    if (viewedCoursesByStudent.get(studentId).containsKey(courseUUID)) {
                        viewedCoursesByStudent.get(studentId).put(courseUUID, viewedCoursesByStudent.get(studentId).get(courseUUID) + 1);
                    } else {
                        viewedCoursesByStudent.get(studentId).put(courseUUID, 1);
                    }
                } else {
                    viewedCoursesByStudent.put(studentId, new HashMap<>());
                    viewedCoursesByStudent.get(studentId).put(courseUUID, 1);
                }
            }
        }

        return viewedCoursesByStudent;
    }
}