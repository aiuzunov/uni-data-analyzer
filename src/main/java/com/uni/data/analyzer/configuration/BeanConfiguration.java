package com.uni.data.analyzer.configuration;

import com.uni.data.analyzer.analyzers.CentralTrendAnalyzer;
import com.uni.data.analyzer.analyzers.CorrelationAnalyzer;
import com.uni.data.analyzer.analyzers.DispersionAnalyzer;
import com.uni.data.analyzer.analyzers.FrequencyAnalyzer;
import com.uni.data.analyzer.analyzers.impl.CentralTrendAnalyzerImpl;
import com.uni.data.analyzer.analyzers.impl.CorrelationAnalyzerImpl;
import com.uni.data.analyzer.analyzers.impl.DispersionAnalyzerImpl;
import com.uni.data.analyzer.analyzers.impl.FrequencyAnalyzerImpl;
import com.uni.data.analyzer.services.CourseService;
import com.uni.data.analyzer.services.FileDataService;
import com.uni.data.analyzer.services.FileParser;
import com.uni.data.analyzer.services.StudentService;
import com.uni.data.analyzer.services.impl.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeanConfiguration {

    @Bean
    public CentralTrendAnalyzer centralTrendAnalyzer() {
        return new CentralTrendAnalyzerImpl();
    }

    @Bean
    public CorrelationAnalyzer correlationAnalyzer() {
        return new CorrelationAnalyzerImpl();
    }

    @Bean
    public DispersionAnalyzer dispersionAnalyzer() {
        return new DispersionAnalyzerImpl();
    }

    @Bean
    public FrequencyAnalyzer frequencyAnalyzer() {
        return new FrequencyAnalyzerImpl();
    }

    @Bean
    public CourseService coursesService() {
        return new CourseServiceImpl();
    }

    @Bean
    public StudentService studentService() {
        return new StudentServiceImpl();
    }

    @Bean
    public FileDataService fileDataService(@Qualifier("csv") FileParser csvFileParser,
                                           @Qualifier("xlsx") FileParser xlsxFileParser) {
        return new FileDataServiceImpl(List.of(csvFileParser, xlsxFileParser));
    }

    @Bean()
    @Qualifier("csv")
    public FileParser csvFileParser() {
        return new CSVFileParser();
    }

    @Bean
    @Qualifier("xlsx")
    public FileParser xlsxFileParser() {
        return new XLSFileParserImpl();
    }
}
