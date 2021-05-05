package com.uni.data.analyzer.controllers;

import com.uni.data.analyzer.services.impl.GenerateImage;
import com.uni.data.analyzer.services.impl.GeneratePDF;
import com.uni.data.analyzer.services.impl.GenerateXLSX;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/analyse")
public class FileDownloadController {

    @PostMapping("/download")
    public ResponseEntity uploadFileForAnalysis(HttpServletResponse response, @RequestParam("selected-analysis") Integer selected_analysis,
                                                @RequestParam("analysis-type") String analysis_type) throws IOException {
        try {

            double[] xData = new double[] { 0.5, 1.0, 2.0};
            double[] yData = new double[] { 2.0, 1.0, 0.0 };

            GeneratePDF generateXLSX = new GeneratePDF(xData,yData);

            byte[] bytes = generateXLSX.GenerateFile();
            System.out.print("TEST "+ selected_analysis + " " + analysis_type);


            response.addHeader("filename","test.pdf");
            response.setContentType("application/pdf");

            return ResponseEntity
                    .ok()
                    .body(bytes);

            /* TODO- Да имплементирам конвертирането на резултата от избраният анализ в файл от селектирания формат */
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }





    }
}
