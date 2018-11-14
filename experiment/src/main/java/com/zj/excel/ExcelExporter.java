package com.zj.excel;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 一个通用的将List<T>中数据导出为Excel文档的工具类
 *
 * @author zhrb@cec.jmu
 */
public class ExcelExporter {

    /**
     * 导出excel到流
     *
     * @param data
     * @param columnNames
     * @param methodNames
     * @param sheetName
     * @param <T>
     * @throws Exception
     */
    public static <T> void excel2013OutputHandle(List<T> data, String[] columnNames, String[] methodNames,
                                                 String sheetName, OutputStream os) throws Exception {
        ExcelEntity<T> excelEntity = new ExcelEntity<>("", columnNames, methodNames, data);
        excelEntity.setSheetName(sheetName);
        Workbook excel = ExcelExporter.export2003Excel(excelEntity);
        excel.write(os);
        os.flush();
        os.close();
    }


    public static void exportMapToExcel2013(List<Map<String, String>> data,
                                            String sheetName, OutputStream os) throws Exception {

        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = excel.createSheet(sheetName);
        exportMapToExcel2003(sheet, data);

        FileOutputStream fos = new FileOutputStream(new File("/Users/shaohuasu/Desktop/aad.xls"));
        excel.write(fos);
        fos.flush();

        excel.write(os);
        os.flush();
        os.close();
    }

    /**
     * 创建表格内容
     *
     * @param sheet 表内容开始的行数
     * @throws Exception
     */
    private static <T> void exportMapToExcel2003(Sheet sheet, List<Map<String, String>> contents) throws Exception {
        int rowIndexBegin = 0;
        if (CollectionUtils.isEmpty(contents)) {
            return;
        }
        List<String> headers = Lists.newArrayList(contents.get(0).keySet());
        int[] columnWidths = new int[headers.size()];
        Row row = sheet.createRow(rowIndexBegin++);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            columnWidths[i] = (headers.get(i).getBytes().length + 2) * 256;
            sheet.setColumnWidth(i, columnWidths[i]);
            fillCell(cell, headers.get(i));
        }

        for (Map<String, String> map : contents) {
            Row con = sheet.createRow(rowIndexBegin++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = con.createCell(i);
                String content = map.getOrDefault(headers.get(i), "");
                int columnWidth = (content.getBytes().length + 2) * 256;
                if (columnWidth > columnWidths[i]) {// 如果实际内容宽度大于对应的表头宽度，则设置为实际内容宽度
                    columnWidths[i] = columnWidth;
                    sheet.setColumnWidth(i, columnWidths[i]);
                }
                fillCell(cell, content);
            }
        }
    }


    public static void fillCell(Cell cell, Object object) {
        if (object.getClass().equals(Date.class)) {// 对日期格式进行特殊处理
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cell.setCellValue(sdf.format((Date) object));
        } else {
            cell.setCellValue(object.toString());
        }
    }


    /**
     * 根据ExcelEntity等参数生成Workbook
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public static <T> Workbook export2007Excel(ExcelEntity<T> entity) throws Exception {
        Workbook workbook = export2007Excel(entity.getHeader(), entity.getFooter(), entity.getSheetName(), entity.getColumnNames(), entity.getMethodNames(),
                entity.getEntities());
        return workbook;
    }


    /**
     * 根据ExcelEntity等参数生成Workbook
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public static <T> Workbook export2003Excel(ExcelEntity<T> entity) throws Exception {
        Workbook workbook = export2003Excel(entity.getHeader(), entity.getFooter(), entity.getSheetName(), entity.getColumnNames(), entity.getMethodNames(),
                entity.getEntities());
        return workbook;
    }


    /**
     * 根据给定参数导出Excel文档
     *
     * @param headerTitle 题头
     * @param footer      脚注
     * @param sheetName
     * @param columnNames 表头名称
     * @param methodNames
     * @param entities
     * @return
     * @throws Exception
     */
    public static <T> Workbook export2003Excel(String headerTitle, String footerTitle, String sheetName, String[] columnNames,
                                               String[] methodNames, List<T> entities) throws Exception {
        if (methodNames.length != columnNames.length)
            throw new IllegalArgumentException("methodNames.length should be equal to columnNames.length:"
                    + columnNames.length + " " + methodNames.length);

        HSSFWorkbook workbook = new HSSFWorkbook();
        //HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFSheet sheet = workbook.createSheet(sheetName);

        //设置题头
        Header header = sheet.getHeader();
        header.setCenter(headerTitle);
        //设置脚注
        Footer footer = sheet.getFooter();
        footer.setCenter(footerTitle);
        int[] columnWidths = new int[columnNames.length];
        // 创建表头
        createTableHeader(sheet, 0, headerTitle, columnNames, columnWidths);
        // 填充表内容
        createTableContent(sheet, 1, methodNames, columnWidths, entities);

        return workbook;

    }


    /**
     * 根据给定参数导出Excel文档
     *
     * @param headerTitle 题头
     * @param footer      脚注
     * @param sheetName
     * @param columnNames 表头名称
     * @param methodNames
     * @param entities
     * @return
     * @throws Exception
     */
    public static <T> Workbook export2007Excel(String headerTitle, String footerTitle, String sheetName, String[] columnNames,
                                               String[] methodNames, List<T> entities) throws Exception {
        if (methodNames.length != columnNames.length)
            throw new IllegalArgumentException("methodNames.length should be equal to columnNames.length:"
                    + columnNames.length + " " + methodNames.length);
        Workbook newWorkBook2007 = new XSSFWorkbook();
        Sheet sheet = newWorkBook2007.createSheet(sheetName);

        //设置题头
        Header header = sheet.getHeader();
        header.setCenter(headerTitle);
        //设置脚注
        Footer footer = sheet.getFooter();
        footer.setCenter(footerTitle);

        int[] columnWidths = new int[columnNames.length];
        // 创建表头
        createTableHeader(sheet, 0, headerTitle, columnNames, columnWidths);
        // 填充表内容
        createTableContent(sheet, 1, methodNames, columnWidths, entities);

        return newWorkBook2007;

    }

    /**
     * 创建表头
     *
     * @param sheet
     * @param index        表头开始的行数
     * @param headerTitle  题头
     * @param columnNames
     * @param columnWidths
     */
    private static void createTableHeader(Sheet sheet, int index, String headerTitle, String[] columnNames,
                                          int[] columnWidths) {


        Row headerRow = sheet.createRow(index);

        /* 格式设置 */
        // 设置字体
        Font font = sheet.getWorkbook().createFont();
        // font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗体显示
        font.setBold(true);
        // 设置背景色
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);

        for (int i = 0; i < columnNames.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellStyle(style);
            headerCell.setCellValue(columnNames[i]);
        }

        for (int i = 0; i < columnNames.length; i++) {
            columnWidths[i] = (columnNames[i].getBytes().length + 2) * 256;
            sheet.setColumnWidth(i, columnWidths[i]);
        }

    }

    /**
     * 创建表格内容
     *
     * @param sheet
     * @param rowIndexBegin 表内容开始的行数
     * @param methodNames   T对象的方法名
     * @param columnWidths
     * @param entities
     * @throws Exception
     */
    private static <T> void createTableContent(Sheet sheet, int rowIndexBegin, String[] methodNames, int[] columnWidths,
                                               List<T> entities) throws Exception {
        Class<? extends Object> clazz = null;
        if (entities.size() > 0)
            clazz = entities.get(0).getClass();

        String content = null;
        for (T t : entities) {

            Row row = sheet.createRow(rowIndexBegin++);
            for (int i = 0; i < methodNames.length; i++) {
                Cell cell = row.createCell(i);
                String getMethodName = "get"
                        + methodNames[i].substring(0, 1).toUpperCase()
                        + methodNames[i].substring(1);
                Method method = clazz.getMethod(getMethodName, null);
                Object object = method.invoke(t, null);

                object = object == null ? "" : object;
                if (object.getClass().equals(Date.class)) {// 对日期格式进行特殊处理
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    content = sdf.format((Date) object);
                    cell.setCellValue(content);
                } else {
                    content = object.toString();
                    cell.setCellValue(content);
                }
                int columnWidth = (content.getBytes().length + 2) * 256;
                if (columnWidth > columnWidths[i]) {// 如果实际内容宽度大于对应的表头宽度，则设置为实际内容宽度
                    columnWidths[i] = columnWidth;
                    sheet.setColumnWidth(i, columnWidths[i]);
                }

            }
        }
    }

    public static <T> void testPOI(String[] columnNames, String[] methodNames, List<T> entities) throws Exception {
        String sheetName = "Test";
        String title = "标题栏";
        String dstFile = "d:/temp/test.xlsx";
        Workbook newWorkBook2007 = new XSSFWorkbook();
        Sheet sheet = newWorkBook2007.createSheet(sheetName);
        int[] columnWidths = new int[columnNames.length];
        // 创建表头
        createTableHeader(sheet, 0, title, columnNames, columnWidths);
        // 填充表内容
        createTableContent(sheet, 1, methodNames, columnWidths, entities);
        // 保存为文件
        saveWorkBook2007(newWorkBook2007, dstFile);
        System.out.println("end");

    }

    /**
     * 将workbook2007村委文件
     *
     * @param workbook2007
     * @param dstFile
     */
    public static void saveWorkBook2007(Workbook workbook2007, String dstFile) {
        File file = new File(dstFile);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            workbook2007.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

}