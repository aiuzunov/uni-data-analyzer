package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.FileData;
import com.uni.data.analyzer.exceptions.IllegalFormatException;
import com.uni.data.analyzer.services.StudentService;

import java.util.*;
import java.util.stream.Collectors;

import static file.definitions.StudentResultsFileColumns.ID;
import static file.definitions.StudentResultsFileColumns.RESULT;

public class StudentServiceImpl implements StudentService {

    @Override
    public Map<String, Double> getStudentGrades(FileData<Double> fileData) {
        Map<String, List<Double>> data = fileData.getData();

        Set<String> studentIds = getStudentIds(data);
        List<Double> studentGrades = data.get(RESULT.getName());

        if (studentIds.size() != studentGrades.size()) {
            throw new IllegalFormatException(String.format(
                    "Невалиден формат на файла с резултати '%s': съществуват колони с рализчни дължини",
                    fileData.getFile().getName()));
        }

        Iterator<String> idIterator = studentIds.iterator();
        Iterator<Double> gradeIterator = studentGrades.iterator();

        return buildMap(idIterator, gradeIterator);
    }

    private Set<String> getStudentIds(Map<String, List<Double>> resultsFile) {
        List<String> ids = resultsFile.get(ID.getName()).stream()
                .map(val -> String.valueOf(val.intValue()))
                .collect(Collectors.toUnmodifiableList());
        return new LinkedHashSet<>(ids);
    }

    private Map<String, Double> buildMap(Iterator<String> firstIterator, Iterator<Double> secondIterator) {
        Map<String, Double> map = new HashMap<>();

        while (firstIterator.hasNext() && secondIterator.hasNext()) {
            map.put(firstIterator.next(), secondIterator.next());
        }

        return map;
    }
}
