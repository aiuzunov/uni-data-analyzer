package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.FileData;
import com.uni.data.analyzer.analyzers.*;
import com.uni.data.analyzer.data.model.UploadedFile;
import com.uni.data.analyzer.data.model.analysis.*;
import com.uni.data.analyzer.data.repositories.AnalysisOperationRepository;
import com.uni.data.analyzer.data.repositories.AnalysisRepository;
import com.uni.data.analyzer.exceptions.MissingRequiredDataException;
import com.uni.data.analyzer.services.AnalyzerService;
import com.uni.data.analyzer.services.CourseService;
import com.uni.data.analyzer.services.FileDataService;
import com.uni.data.analyzer.services.StudentService;
import org.springframework.stereotype.Service;
import utils.data.MultiValueMap;
import utils.data.impl.MultiValueMapImpl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.uni.data.analyzer.analyzers.AnalysisName.*;
import static com.uni.data.analyzer.data.mapper.AnalysisMapper.toDomainObject;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static utils.ArgumentValidationUtils.requireNonNull;
import static utils.DataUtils.buildFrequencyMap;

@Service
public class AnalyzerServiceImpl implements AnalyzerService {

    private final CourseService courseService;
    private final FileDataService fileDataService;
    private final StudentService studentService;

    private final AnalysisOperationRepository analysisOperationRepository;
    private final AnalysisRepository analysisRepository;

    private final CentralTrendAnalyzer centralTrendAnalyzer;
    private final CorrelationAnalyzer correlationAnalyzer;
    private final DispersionAnalyzer dispersionAnalyzer;
    private final FrequencyAnalyzer frequencyAnalyzer;

    private final Map<AnalysisName, Function<String, Map<String, Object>>> analysisFactory = Map.of(
            CORRELATION, this::createCorrelationAnalysis,
            CENTRAL_TREND, this::createCentralTrendAnalysis,
            DISPERSION, this::createDispersionAnalysis,
            FREQUENCY, this::createFrequencyAnalysis
    );

    public AnalyzerServiceImpl(CourseService courseService,
                               FileDataService fileDataService,
                               StudentService studentService,
                               CentralTrendAnalyzer centralTrendAnalyzer,
                               CorrelationAnalyzer correlationAnalyzer,
                               DispersionAnalyzer dispersionAnalyzer,
                               FrequencyAnalyzer frequencyAnalyzer,
                               AnalysisOperationRepository analysisOperationRepository,
                               AnalysisRepository analysisRepository) {
        this.studentService = requireNonNull(studentService, "studentService");
        this.fileDataService = requireNonNull(fileDataService, "fileDataService");
        this.courseService = requireNonNull(courseService, "coursesService");

        this.centralTrendAnalyzer = requireNonNull(centralTrendAnalyzer, "centralTrendAnalyzer");
        this.correlationAnalyzer = requireNonNull(correlationAnalyzer, "correlationAnalyzer");
        this.dispersionAnalyzer = requireNonNull(dispersionAnalyzer, "dispersionAnalyzer");
        this.frequencyAnalyzer = requireNonNull(frequencyAnalyzer, "frequencyAnalyzer");

        this.analysisRepository = requireNonNull(analysisRepository, "analysisRepository");
        this.analysisOperationRepository = requireNonNull(analysisOperationRepository, "analysisOperationRepository");
    }

    @Override
    public Map<String, Object> createAnalysis(String analysisName, String sessionId) {
        AnalysisName name = map(analysisName);
        return analysisFactory.get(name).apply(sessionId);
    }

    private Map<String, Object> createCentralTrendAnalysis(String sessionId) {
        AnalysisOperation operation = analysisOperationRepository.findFirstBySessionIdOrderByIdDesc(sessionId);
        Set<UploadedFile> files = getUploadedFiles(operation, CENTRAL_TREND.getName());

        UploadedFile logFile = fileDataService.getRequiredLogFile(files);
        Map<String, Object> savedAnalysisValues = getSavedAnalysisValues(CentralTrendAnalysis.class, logFile);
        if (!savedAnalysisValues.isEmpty()) {
            return savedAnalysisValues;
        }

        FileData<String> logFileData = fileDataService.parseFile(logFile);
        logFileData.getError().ifPresent(error ->
                saveErrorAnalysisAndThrowException(operation, logFile, error, CENTRAL_TREND, CentralTrendAnalysis::new));

        return calculateCentralTrendAnalysis(operation, logFileData);
    }

    private Map<String, Object> calculateCentralTrendAnalysis(AnalysisOperation operation, FileData<String> logFile) {
        List<String> viewedCourses = courseService.getViewedCourses(logFile.getData());
        Map<String, Integer> viewsCountByCourse = buildFrequencyMap(viewedCourses);

        CentralTrendAnalysis analysis = toDomainObject(centralTrendAnalyzer.analyze(new ArrayList<>(viewsCountByCourse.values())));
        analysis.getFiles().add(logFile.getFile());
        operation.getAnalysis().put(CENTRAL_TREND, analysis);

        analysisRepository.save(analysis);
        analysisOperationRepository.save(operation);

        return analysis.getValues();
    }

    private Map<String, Object> createCorrelationAnalysis(String sessionId) {
        Iterable<AnalysisOperation> all = analysisOperationRepository.findAll();
        AnalysisOperation operation = analysisOperationRepository.findFirstBySessionIdOrderByIdDesc(sessionId);
        Set<UploadedFile> files = getUploadedFiles(operation, CORRELATION.getName());

        // get files
        UploadedFile logFile = fileDataService.getRequiredLogFile(files);
        List<UploadedFile> resultFiles = fileDataService.getRequiredResultsFiles(files);
        List<UploadedFile> filesForAnalysis = new ArrayList<>(resultFiles) {{
            add(logFile);
        }};

        // Check for previously calculated analysis
//        Map<String, Object> savedAnalysis = getSavedAnalysisValues(CorrelationAnalysis.class, filesForAnalysis);
//        if (!savedAnalysis.isEmpty()) {
//            return savedAnalysis;
//        }

        // Parse files
        FileData<String> parsedLogFile = fileDataService.parseFile(logFile);
        List<FileData<Double>> parsedResultsFiles = resultFiles.stream().map(fileDataService::parseFileNumeric)
                .collect(Collectors.toUnmodifiableList());

        // Return error if log parsing fails
        parsedLogFile.getError().ifPresent(error -> {
            saveErrorAnalysisAndThrowException(operation, filesForAnalysis, List.of(error));
        });

        // Return error if results parsing fails
        List<String> errors = parsedResultsFiles.stream().flatMap(parsedFile -> parsedFile.getError().isPresent() ?
                Stream.of(parsedFile.getError().get()) :
                Stream.empty())
                .collect(Collectors.toUnmodifiableList());
        if (!errors.isEmpty()) {
            saveErrorAnalysisAndThrowException(operation, filesForAnalysis, errors);
        }

        return calculateCorrelationAnalysis(operation, parsedLogFile, parsedResultsFiles);

    }

    private void saveErrorAnalysisAndThrowException(AnalysisOperation operation,
                                                    UploadedFile fileForAnalysis,
                                                    String error,
                                                    AnalysisName analysisName,
                                                    Function<String, Analysis> getErrorAnalysis) {
        Analysis errorAnalysis = getErrorAnalysis.apply(error);
        errorAnalysis.getFiles().add(fileForAnalysis);
        operation.getAnalysis().put(analysisName, errorAnalysis);

        analysisRepository.save(errorAnalysis);
        analysisOperationRepository.save(operation);
        throw new MissingRequiredDataException(error);
    }

    private void saveErrorAnalysisAndThrowException(AnalysisOperation operation, List<UploadedFile> filesForAnalysis, List<String> errors) {
        String error = String.join(",", errors);
        CorrelationAnalysis errorAnalysis = new CorrelationAnalysis(error);

        errorAnalysis.getFiles().addAll(filesForAnalysis);
        operation.getAnalysis().put(CORRELATION, errorAnalysis);

        analysisRepository.save(errorAnalysis);
        analysisOperationRepository.save(operation);

        throw new MissingRequiredDataException(error);
    }

    private Map<String, Object> calculateCorrelationAnalysis(AnalysisOperation operation, FileData<String> logsFile, List<FileData<Double>> resultsFiles) {
        // correlation between student grade & frequency distribution on number of viewed courses
        Map<String, Integer> absoluteFrequencyOnViewedCoursesPerStudent = getAbsoluteFrequencyOnViewedCoursesPerStudent(logsFile);
        Map<String, com.uni.data.analyzer.analyzers.analysis.CorrelationAnalysis> analysisData = new HashMap<>();

        for (FileData<Double> resultsFile : resultsFiles) {
            Map<String, Double> studentGrades = studentService.getStudentGrades(resultsFile);

            MultiValueMap<String, Double, Double> studentGradeViewsFrequency = new MultiValueMapImpl<>();

            for (Map.Entry<String, Double> entry : studentGrades.entrySet()) {
                double viewCount = ofNullable(absoluteFrequencyOnViewedCoursesPerStudent.get(entry.getKey())).orElse(0);
                studentGradeViewsFrequency.put(entry.getKey(), entry.getValue(), viewCount);
            }

            analysisData.put(resultsFile.getFile().getName(), correlationAnalyzer.analyze(studentGradeViewsFrequency));
        }

        CorrelationAnalysis correlationAnalysis = toDomainObject(analysisData);

        List<UploadedFile> filesForAnalysis = resultsFiles.stream().map(FileData::getFile).collect(Collectors.toList());
        filesForAnalysis.add(logsFile.getFile());

        correlationAnalysis.getFiles().addAll(filesForAnalysis);
        operation.getAnalysis().put(CORRELATION, correlationAnalysis);

        analysisRepository.save(correlationAnalysis);
        analysisOperationRepository.save(operation);

        return correlationAnalysis.getValues();
    }

    private Map<String, Object> createDispersionAnalysis(String sessionId) {
        AnalysisOperation operation = analysisOperationRepository.findFirstBySessionIdOrderByIdDesc(sessionId);
        Set<UploadedFile> files = getUploadedFiles(operation, DISPERSION.getName());

        UploadedFile logFile = fileDataService.getRequiredLogFile(files);
        Map<String, Object> savedAnalysis = getSavedAnalysisValues(DispersionAnalysis.class, logFile);
        if (!savedAnalysis.isEmpty()) {
            return savedAnalysis;
        }

        FileData<String> logFileData = fileDataService.parseFile(logFile);
        return calculateDispersionAnalysis(operation, logFileData);
    }

    private Map<String, Object> calculateDispersionAnalysis(AnalysisOperation operation, FileData<String> logFile) {
        List<String> viewedCourses = courseService.getViewedCourses(logFile.getData());
        Map<String, Integer> viewsCountByCourse = buildFrequencyMap(viewedCourses);

        DispersionAnalysis analysis = toDomainObject(dispersionAnalyzer.analyze(viewsCountByCourse.values()));
        analysis.getFiles().add(logFile.getFile());
        operation.getAnalysis().put(DISPERSION, analysis);

        analysisRepository.save(analysis);
        analysisOperationRepository.save(operation);

        return analysis.getValues();
    }

    private Map<String, Integer> getAbsoluteFrequencyOnViewedCoursesPerStudent(FileData<String> fileData) {
        Map<String, Map<String, Integer>> viewedCoursesByStudent = courseService.getViewedCoursesByStudent(fileData);

        Map<String, Integer> studentCoursesViews = viewedCoursesByStudent.entrySet().stream()
                .flatMap(entry -> Stream.of(Map.entry(entry.getKey(), entry.getValue().values().stream()
                        .mapToInt(value -> value)
                        .sum())))
                .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        return frequencyAnalyzer.analyze(studentCoursesViews).getEntries()
                .stream().map(entry -> Map.entry(entry.getName(), entry.getAbsoluteFrequency()))
                .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, Object> createFrequencyAnalysis(String sessionId) {
        AnalysisOperation operation = analysisOperationRepository.findFirstBySessionIdOrderByIdDesc(sessionId);
        Set<UploadedFile> files = getUploadedFiles(operation, FREQUENCY.getName());

        UploadedFile logFile = fileDataService.getRequiredLogFile(files);
        Map<String, Object> savedAnalysis = getSavedAnalysisValues(FrequencyAnalysis.class, logFile);
        if (!savedAnalysis.isEmpty()) {
            return savedAnalysis;
        }

        FileData<String> logFileData = fileDataService.parseFile(logFile);
        return calculateFrequencyAnalysis(operation, logFileData);
    }

    private Map<String, Object> calculateFrequencyAnalysis(AnalysisOperation operation, FileData<String> logFile) {
        List<String> viewedCourses = courseService.getViewedCourses(logFile.getData());
        Map<String, Integer> viewsCountByCourse = buildFrequencyMap(viewedCourses);

        FrequencyAnalysis analysis = toDomainObject(frequencyAnalyzer.analyze(viewsCountByCourse));
        analysis.getFiles().add(logFile.getFile());
        operation.getAnalysis().put(FREQUENCY, analysis);

        analysisRepository.save(analysis);
        analysisOperationRepository.save(operation);

        return analysis.getValues();
    }

    private Map<String, Object> getSavedAnalysisValues(Class<? extends Analysis> type, UploadedFile file) {
        return getSavedAnalysisValues(type, List.of(file));
    }

    private Map<String, Object> getSavedAnalysisValues(Class<?> type, List<UploadedFile> files) {
        List<Analysis> analysisList = analysisRepository.findAllByFilesIn(files);

        List<Analysis> calculatedAnalysis = analysisList.stream()
                .filter(analysis -> analysis.getFiles().size() == files.size())
                .filter(type::isInstance)
                .collect(Collectors.toUnmodifiableList());

        return calculatedAnalysis.size() == 0 ?
                Map.of() :
                calculatedAnalysis.get(calculatedAnalysis.size() - 1).getValues();
    }

    private Set<UploadedFile> getUploadedFiles(AnalysisOperation operation, String analysisName) {
        if (operation == null || operation.getUploadedFiles() == null) {
            throw new MissingRequiredDataException(
                    String.format("Трябва да се качат необхидмите файлове преди генериране на '%s' анализ", analysisName));
        }

        return operation.getUploadedFiles();
    }
}