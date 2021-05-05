package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.services.FileDownloadService;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class GenerateImage implements FileDownloadService {

    private double[] xData;
    private double[] yData;

    public GenerateImage(double[] xData, double[] yData)
    {
        this.xData = xData;
        this.yData = yData;
    }

    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }


    @Override
    public double[][] parseJSON(Map<String, Object> json) throws IOException{
        // TODO: Implement
        double[] xData = new double[] { 0.0, 1.0, 2.0 };

        double[] yData = new double[] { 2.0, 1.0, 0.0 };

        double[][] kekw = {xData, yData};
        return kekw;
    }

    @Override
    public byte[] GenerateFile() throws IOException{

        XYChart chart = QuickChart.getChart("Резултат от анализ", "x", "y", "y(x)", xData, yData);

        BufferedImage image = BitmapEncoder.getBufferedImage(chart);

        byte[] bytes = toByteArray(image, "jpg");

        return bytes;
    }
}
