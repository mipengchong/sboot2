package com.zj;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;


public class CsvToExcel {

    public final static String basePath = "/Users/shaohuasu/Desktop/运营/";

    @Test
    public void daily() throws Exception{
        convertCsvToXlsx(basePath + "1201n.csv", basePath + "1201n.xlsx");
    }


    public static void convertCsvToXlsx(String csvLocation, String xlsLocation) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("Sheet");
        AtomicReference<Integer> row = new AtomicReference<>(0);
        List<Integer> columnWidths = Lists.newArrayList();
        try {
            Files.lines(Paths.get(csvLocation)).forEach(line -> {
                Row currentRow = sheet.createRow(row.getAndSet(row.get() + 1));
                String[] nextLine = line.split(",");
                Stream.iterate(0, i -> i + 1).limit(nextLine.length).forEach(i -> {
                    processWidth(sheet, columnWidths, nextLine, i);
                    currentRow.createCell(i).setCellValue(nextLine[i]);
                });
            });
        } catch (NoSuchFileException e) {
            System.out.println("请检查CSV文件路径：" + csvLocation);
        }
        FileOutputStream fos = new FileOutputStream(new File(xlsLocation));
        workbook.write(fos);
        fos.flush();
    }


    private static void processWidth(SXSSFSheet sheet, List<Integer> columnWidths, String[] nextLine, Integer i) {
        int columnWidth = (nextLine[i].getBytes().length + 2) * 256;
        if (columnWidths.size() <= i) {
            columnWidths.add(columnWidth);
        }
        if (columnWidth > Optional.ofNullable(columnWidths.get(i)).orElse(0)) {
            columnWidths.set(i, columnWidth);
            sheet.setColumnWidth(i, columnWidths.get(i));
        }
    }
}
