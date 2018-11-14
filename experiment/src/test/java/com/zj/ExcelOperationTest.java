package com.zj;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zj.excel.ExcelEntity;
import com.zj.excel.ExcelExporter;
import com.zj.zjtools.time.DateUtils;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zj.ExcelTest.getCellValue;

public class ExcelOperationTest {

    public final static String basePath = "/Users/shaohuasu/Desktop/运营/";

    @Test
    public void readTest() throws Exception {
        String dealPath = basePath + "10GROSS3.xlsx";
        List<DealAmount> amounts = processDealExcel(dealPath);

        String cityDbPath = basePath + "BDS1108.xlsx";
        List<CityDB> cityDBS = processDBExcel(cityDbPath);

        cityDBS.stream().filter(Objects::nonNull).forEach(db -> {
            List<DealAmount> incomeList = amounts.stream()
                    .filter(r -> Objects.nonNull(r.getGrossIncome()))
                    .filter(r -> Objects.nonNull(db.getValidStartDay()) && Objects.nonNull(db.getEndDay()))
                    .filter(r -> r.getCommunityId().equals(db.getCommunityId()))
                    .filter(r -> db.getValidStartDay() <= r.getOrderDay() && db.getEndDay() >= r.getOrderDay())
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(incomeList)) {
                db.setGrossIncome(BigDecimal.valueOf(incomeList.stream().mapToDouble(DealAmount::getGrossIncome).sum())
                        .setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());

            }


            List<DealAmount> incomeList0 = amounts.stream()
                    .filter(r -> Objects.nonNull(r.getGrossIncome()))
                    .filter(r -> Objects.nonNull(db.getStartDay0()) && Objects.nonNull(db.getEndDay0()))
                    .filter(r -> r.getCommunityId().equals(db.getCommunityId()))
                    .filter(r -> db.getStartDay0() <= r.getOrderDay() && db.getEndDay0() >= r.getOrderDay())
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(incomeList0)) {
                db.setGrossIncome(BigDecimal.valueOf(incomeList0.stream().mapToDouble(DealAmount::getGrossIncome).sum())
                        .setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
            }
        });

        String[] columnNames = {"社区ID","有效开始时间","结束时间","毛收入",
                "社区ID","开始负责时间（如有）","结束时间","毛收入"};
        String[] methodNames = {"communityId", "validStartDay", "endDay", "grossIncome",
                "communityId0", "startDay0","endDay0","grossIncome0"
        };

        ExcelEntity<CityDB> excelEntity = new ExcelEntity<>("", columnNames, methodNames, cityDBS);
        excelEntity.setSheetName("城市DB详情");
        Workbook excel = ExcelExporter.export2007Excel(excelEntity);
        OutputStream ouputStream = new FileOutputStream(new File(basePath + "full.xlsx"));
        excel.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    private List<CityDB> processDBExcel(String path) throws Exception {
        List<CityDB> dbs = Lists.newArrayList();
        XSSFSheet sheet = new XSSFWorkbook(new FileInputStream(new File(path)))
                .getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            List<Object> rowValue = Lists.newArrayListWithCapacity(8);
            for (Cell cell : row) {
                Object re = getCellValue(cell, cell.getCellType());
                rowValue.add(re);
            }

            int check = 8 - rowValue.size();
            if (check > 0) {
                for (int i = 0; i < check; i++) {
                    rowValue.add("");
                }
            }
            CityDB db = new CityDB();
            try {
                db.setCommunityId(getLong(rowValue.get(0)));
                db.setValidStartDay(getInt(rowValue.get(1)));
                db.setEndDay(getInt(rowValue.get(2)));
                db.setGrossIncome(getString(rowValue.get(3)));

                db.setCommunityId0(getLong(rowValue.get(4)));
                db.setStartDay0(getInt(rowValue.get(5)));
                db.setEndDay0(getInt(rowValue.get(6)));
                db.setGrossIncome0(getString(rowValue.get(7)));
            } catch (Exception e) {
                System.out.println("rowValue=====>:" + JSON.toJSONString(rowValue));
            }
            dbs.add(db);
        }
        return dbs;
    }


    /**
     * 处理交易数据
     *
     * @param path
     * @return
     * @throws Exception
     */
    private List<DealAmount> processDealExcel(String path) throws Exception {
        XSSFSheet sheet = new XSSFWorkbook(new FileInputStream(new File(path)))
                .getSheetAt(0);
        List<DealAmount> amounts = Lists.newArrayList();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            List<Object> rowValue = Lists.newArrayListWithCapacity(9);
            for (Cell cell : row) {
                Object re = getCellValue(cell, cell.getCellType());
                rowValue.add(re);
            }
            DealAmount amount = new DealAmount();
            amount.setCommunityId(getLong(rowValue.get(0)));
            amount.setOrderDay(getInt(rowValue.get(2)));
            amount.setGrossIncome(getDouble(rowValue.get(8)));
            amounts.add(amount);
        }
        return amounts;
    }

    @Data
    public class DealAmount {
        private Long communityId;
        private String communityName;
        private Integer orderDay;
        private String skuName;
        private String skuSpec;
        private String orderAmount;
        private Double grossRatio;
        private Double grossIncome;
    }


    public static Long getLong(Object o) {
        if (Objects.isNull(o) || StringUtils.isBlank(o.toString()) || o.toString().equals("null")) {
            return null;
        }
        return new BigDecimal(String.valueOf(o).trim()).longValue();
    }

    public static Integer getInt(Object o) {
        if (Objects.isNull(o) || StringUtils.isBlank(o.toString()) || o.toString().equals("null")) {
            return null;
        }
        String val = String.valueOf(o).replaceAll("[-/]", "").trim();
        try {
            if (val.contains("月") && val.contains("日")) {
                return new BigDecimal(new SimpleDateFormat("2018MMdd")
                        .format(DateUtils.parse(val, "MM月dd日"))).intValue();
            }
            return new BigDecimal(val).intValue();
        } catch (Exception e) {
            System.out.println("日期格式化错误===> " + o.toString());
        }
        return null;
    }

    public static Double getDouble(Object o) {
        if (Objects.isNull(o) || StringUtils.isBlank(o.toString()) || o.toString().equals("null")) {
            return null;
        }
        return new BigDecimal(String.valueOf(o).trim()).doubleValue();
    }

    public static String getString(Object o) {
        if (Objects.isNull(o) || StringUtils.isBlank(o.toString()) || o.toString().equals("null")) {
            return null;
        }
        return String.valueOf(o);
    }


    @Data
    public class CityDB {
        private Long communityId;
        private Integer validStartDay;
        private Integer endDay;
        private String grossIncome;

        private Long communityId0;
        private Integer startDay0;
        private Integer endDay0;
        private String grossIncome0;
    }
}
