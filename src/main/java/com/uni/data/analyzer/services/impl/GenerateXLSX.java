package com.uni.data.analyzer.services.impl;


import com.uni.data.analyzer.services.FileDownloadService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class GenerateXLSX implements FileDownloadService {


    private double[] xData;
    private double[] yData;

    public GenerateXLSX(double[] xData, double[] yData)
    {
        this.xData = xData;
        this.yData = yData;
    }

    @Override
    public double[][] parseJSON(Map<String, Object> json) throws IOException{
        // TODO: Implement
        double[][] kekw = {xData, yData};
        return kekw;
    }

    @Override
    public byte[] GenerateFile() throws IOException{


        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Persons");

        Row header = sheet.createRow(0);


        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("X");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Y");
        headerCell.setCellStyle(headerStyle);

        Integer xlength = xData.length;
        Integer ylength = yData.length;
        Integer maxlength;

        if(xlength > ylength || xlength == ylength)
        {
            maxlength = xlength;
        }
        else
        {
            maxlength = ylength;
        }

        Integer x_row = 1;
        for (double cellValue : xData)
        {
            Row row = sheet.createRow(x_row);
            x_row ++;
            Cell xCell = row.createCell(0);
            xCell.setCellValue(cellValue);
        }

        Integer y_row = 1;
        for (double cellValue : yData)
        {
            Row row = sheet.getRow(y_row);

            if(row == null)
            {
                row = sheet.createRow(y_row);
            }

            y_row ++;
            Cell yCell = row.createCell(1);
            yCell.setCellValue(cellValue);
        }
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 3, 3, 14, 26);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Анализ");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("X");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Y");

        Double[] xDataConverted = new Double[maxlength];
        Double[] yDataConverted = new Double[maxlength];

        Integer i = 0 ;
        for (Double val : xData)
        {
            xDataConverted[i] = val;
            yDataConverted[i] = 0.0;
            i++;
        }

        i=0;

        for (Double val : yData)
        {
            yDataConverted[i] = val;

            if(i > xlength)
            {
                xDataConverted[i] = 0.0;
            }

            i++;
        }

        XDDFNumericalDataSource<Double> xvals = XDDFDataSourcesFactory.fromArray(xDataConverted);

        XDDFNumericalDataSource<Double> yvals = XDDFDataSourcesFactory.fromArray(yDataConverted);

        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

        XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(xvals, yvals);
        series1.setTitle("Y", null);
        series1.setSmooth(false);
        chart.plot(data);


        File currDir = new File("./Sample_Chart.xlsx");

        FileOutputStream outputStream = new FileOutputStream(currDir);
        workbook.write(outputStream);
        workbook.close();

        byte[] bytes = new byte[(int) currDir.length()];


        FileInputStream fis = null;
        try {

            fis = new FileInputStream(currDir);

            fis.read(bytes);

        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return bytes;
    }
}
