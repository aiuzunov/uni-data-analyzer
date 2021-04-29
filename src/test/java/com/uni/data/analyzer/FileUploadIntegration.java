package com.uni.data.analyzer;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.uni.data.analyzer.data.repositories.AnalysisOperationRepository;
import com.uni.data.analyzer.data.repositories.UploadedFilesRepository;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.io.IOUtil;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UniDataAnalyzerApplication.class, WebConfig.class})
@WebAppConfiguration
public class FileUploadIntegration {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UploadedFilesRepository filesRepository;

    @Autowired
    private AnalysisOperationRepository analysisRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void wrongFormatUploadOnXls() throws Exception {
        var firstFile = new MockMultipartFile("files", "filename.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "some xml".getBytes());
        var secondFile = new MockMultipartFile("files", "filename2.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "some xml".getBytes());

        mockMvc.perform(multipart("/analyse/upload/files")
                .file(firstFile)
                .file(secondFile))
                .andExpect(status().is(422));
    }

    @Test
    public void wrongFormatUploadOnZip() throws Exception {
        var firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());

        mockMvc.perform(multipart("/analyse/upload")
                .file(firstFile))
                .andExpect(status().is(422));
    }

    @Test
    public void uploadXls() throws Exception {
        var firstFile = new MockMultipartFile("files", "Test1.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                this.getClass().getClassLoader().getResourceAsStream("Test1.xlsx").readAllBytes());

        var secondFile = new MockMultipartFile("files", "Test2.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                this.getClass().getClassLoader().getResourceAsStream("Test2.xlsx").readAllBytes());

        mockMvc.perform(multipart("/analyse/upload/files")
                .file(firstFile)
                .file(secondFile))
                .andExpect(status().is(200));
    }

    @Test
    public void uploadCsv() throws Exception {
        var firstFile = new MockMultipartFile("files", "Test1.csv",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                this.getClass().getClassLoader().getResourceAsStream("Test1.csv").readAllBytes());

        var secondFile = new MockMultipartFile("files", "Test2.csv",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                this.getClass().getClassLoader().getResourceAsStream("Test2.csv").readAllBytes());

        mockMvc.perform(multipart("/analyse/upload/files")
                .file(firstFile)
                .file(secondFile))
                .andExpect(status().is(200));
    }

    @Test
    public void uploadZip() throws Exception {
        var zipFile =  new MockMultipartFile("file", "InputData.zip", "application/zip",
                this.getClass().getClassLoader().getResourceAsStream("InputData.zip").readAllBytes());

        mockMvc.perform(multipart("/analyse/upload")
                .file(zipFile))
                .andExpect(status().is(200));
    }
}
