package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.services.FileDownloadService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.knowm.xchart.*;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class GeneratePDF implements FileDownloadService {

    private double[] xData;
    private double[] yData;

    public GeneratePDF(double[] xData, double[] yData)
    {
        this.xData = xData;
        this.yData = yData;
    }
    private BufferedImage createChart(int width, int height) {

        XYChart chart = QuickChart.getChart("Резултат от анализ", "x", "y", "y(x)", xData, yData);

        return BitmapEncoder.getBufferedImage(chart);
    }


    @Override
    public double[][] parseJSON(Map<String, Object> json) throws IOException {
        // TODO: Implement
        double[] xData = new double[] { 0.0, 1.0, 2.0 };

        double[] yData = new double[] { 2.0, 1.0, 0.0 };

        double[][] kekw = {xData, yData};
        return kekw;
    }

    @Override
    public byte[] GenerateFile() throws IOException {

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);

            page.setRotation(90);

            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDImageXObject chartImage = JPEGFactory.createFromImage(document,
                    createChart((int) pageHeight, (int) pageWidth));

            contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
            contentStream.drawImage(chartImage, 0, 0);
            contentStream.close();

            document.addPage(page);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

}