package com.zj.excel;

import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


/**
 * 一个通用的将List<T>中数据导出为Excel文档的工具类
 *
 * @author zhrb@cec.jmu
 */
public class ExcelExporterExtend {

    public static <T> Workbook export2003Excel(Consumer<Map> headProcess, ExcelEntity<T>... entitys) throws Exception {
        Workbook workbook = export2003ExcelWithMultiSheets(entitys, headProcess);
        return workbook;
    }


    private static <T> Workbook export2003ExcelWithMultiSheets(ExcelEntity<T>[] entitys, Consumer<Map> headProcess) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Arrays.stream(entitys).forEach(entity -> {
            HSSFSheet sheet = workbook.createSheet(entity.getSheetName());
            //设置题头
            Header header = sheet.getHeader();
            header.setCenter(entity.getHeader());
            //设置脚注
            Footer footer = sheet.getFooter();
            footer.setCenter(entity.getFooter());
            // 创建表头
            Map map = Maps.newHashMap();
            map.put("sheet", sheet);
            map.put("entity", entity);
            headProcess.accept(map);
            // 填充表内容
            // 填充表内容
            try {
                createTableContent(sheet, Optional.ofNullable(entity.getRowIndexBegin()).orElse(1),
                        entity.getMethodNames(), entity.getColumnWidths(), entity.getEntities());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return workbook;
    }




    public static <T> Workbook export2007Excel(Consumer<Map> headProcess, ExcelEntity<T>... entitys) throws Exception {
        Workbook workbook = export2007ExcelWithMultiSheets(entitys, headProcess);
        return workbook;
    }

    private static <T> Workbook export2007ExcelWithMultiSheets(ExcelEntity<T>[] entitys, Consumer<Map> headProcess) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Arrays.stream(entitys).filter(Objects::nonNull).forEach(entity -> {
            XSSFSheet sheet = workbook.createSheet(entity.getSheetName());
            //设置题头
            Header header = sheet.getHeader();
            header.setCenter(entity.getHeader());
            //设置脚注
            Footer footer = sheet.getFooter();
            footer.setCenter(entity.getFooter());
            // 创建表头
            Map map = Maps.newHashMap();
            map.put("sheet", sheet);
            map.put("entity", entity);
            headProcess.accept(map);
            // 填充表内容
            try {
                createTableContent(sheet, Optional.ofNullable(entity.getRowIndexBegin()).orElse(1),
                        entity.getMethodNames(), entity.getColumnWidths(), entity.getEntities());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return workbook;
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

}