package com.uni.data.analyzer.analyzers.impl;

import com.uni.data.analyzer.analyzers.CorrelationAnalyzer;
import com.uni.data.analyzer.analyzers.analysis.CorrelationAnalysis;
import utils.data.MultiValueMap;
import utils.data.Tuple;

import java.util.function.Supplier;
import java.util.stream.DoubleStream;

import static com.uni.data.analyzer.analyzers.AnalysisName.CORRELATION;
import static utils.AnalysisValidationUtils.requireNonEmpty;

public class CorrelationAnalyzerImpl implements CorrelationAnalyzer {

    // зависимостта между оценката на всеки един студент за дадената дисциплина и
    // честотното разрепделение на данните от (Вариант Б) брой качени файлове в системата

    @Override
    public CorrelationAnalysis analyze(MultiValueMap<String, Double, Double> map) {
        requireNonEmpty(map, CORRELATION.getName());
        int count = map.size();

        Supplier<DoubleStream> firstValues = () -> map.getValues().stream().mapToDouble(Tuple::getFirst);
        Supplier<DoubleStream> secondValues = () -> map.getValues().stream().mapToDouble(Tuple::getSecond);

        double firstSum = firstValues.get().sum();
        double secondSum = secondValues.get().sum();

        double firstSquaredSum = calculateSquaredSum(firstValues.get());
        double secondSquaredSum = calculateSquaredSum(secondValues.get());

        double multiplicationSum = calculateMultiplicationSum(map);

        double correlationCoefficient =
                calculateCorrelationCoefficient(count, firstSum, secondSum, firstSquaredSum, secondSquaredSum, multiplicationSum);
        return new CorrelationAnalysis(correlationCoefficient);
    }

    private double calculateSquaredSum(DoubleStream stream) {
        return stream.map(number -> Math.pow(number, 2)).sum();
    }

    private double calculateMultiplicationSum(MultiValueMap<String, Double, Double> map) {
        return map.getValues().stream()
                .mapToDouble(tuple -> tuple.getFirst() * tuple.getSecond())
                .sum();
    }

    private double calculateCorrelationCoefficient(int count, double firstSum, double secondSum, double firstSquaredSum, double secondSquaredSum, double multiplicationSum) {
        double numerator = count * multiplicationSum - firstSum * secondSum;

        double denominator = Math.sqrt(
                (count * firstSquaredSum - Math.pow(firstSum, 2)) *
                        (count * secondSquaredSum - Math.pow(secondSum, 2))
        );

        return numerator / denominator;
    }
}