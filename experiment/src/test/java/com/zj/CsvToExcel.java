package com.zj;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class CsvToExcel {

    public final static String basePath = "/Users/shaohuasu/Desktop/运营/";

    @Test
    public void daily() throws Exception{
        convertCsvToXlsx(basePath + "qingdao1114.csv", basePath + "qingdao1114.xlsx");
    }


    public static void convertCsvToXlsx(String csvLocation, String xlsLocation) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("Sheet");
        AtomicReference<Integer> row = new AtomicReference<>(0);
        Files.readAllLines(Paths.get(csvLocation)).forEach(line -> {
            Row currentRow = sheet.createRow(row.getAndSet(row.get() + 1));
            String[] nextLine = line.split(",");
            Stream.iterate(0, i -> i + 1).limit(nextLine.length).forEach(i -> {
                currentRow.createCell(i).setCellValue(nextLine[i]);
            });
        });
        FileOutputStream fos = new FileOutputStream(new File(xlsLocation));
        workbook.write(fos);
        fos.flush();
    }
}