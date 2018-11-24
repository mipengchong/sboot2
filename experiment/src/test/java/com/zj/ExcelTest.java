package com.zj;

import com.google.common.collect.Lists;
import com.zj.web.excel.ExcelEntity;
import com.zj.web.excel.ExcelExporter;
import com.zj.zjtools.time.DateUtils;
import lombok.Builder;
import lombok.Data;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExcelTest {

    public final static String basePath = "/Users/shaohuasu/Desktop/运营/";

    @Test
    public void readTest() throws Exception {
        String dealPath = basePath + "10DEAL.xlsx";
        List<DealAmount> amounts = processDealExcel(dealPath);

        String cityDbPath = basePath + "BD.xlsx";
        List<CityDB> cityDBS = processDBExcel(cityDbPath);

        Map<String, CityDB> cityDBMap = cityDBS.stream()
                .filter(r -> Objects.nonNull(r.getCommunityId()) && Objects.nonNull(r.getHeadMan()) && Objects.nonNull(r.getOpenTime()) && Objects.nonNull(r.getHireTime()))
                .peek(d -> {
                    if (d.getHireTime() >= d.getOpenTime()) {
                        d.setUseTime(d.getHireTime());
                    } else {
                        d.setUseTime(d.getOpenTime());
                    }
                })
                .collect(Collectors.toMap(k -> k.getCommunityId() + "_" + k.getHeadMan(), Function.identity()));

        cityDBMap.forEach((idx, cityDB) -> {
            Long communityId = Long.valueOf(idx.split("_")[0]);
            String headMan = idx.split("_")[1];
            double realDeal = amounts.stream()
                    .filter(r -> r.getCommunityId().equals(communityId) && r.getHeadMan().equals(headMan) && cityDB.getUseTime() <= r.getDate())
                    .mapToDouble(DealAmount::getRealDeal).sum();
            cityDB.setRealDeal(BigDecimal.valueOf(realDeal).setScale(2, RoundingMode.HALF_UP).doubleValue());
        });

        String[] columnNames = {"ID", "城市", "社区ID", "社区名称", "首次开团时间",
                "目前社区业务负责人", "岗位", "入职时间", "开团日期", "成交额"};
        String[] methodNames = {"id", "cityName", "communityId", "communityName", "firstOpenTime",
                "headMan", "position", "hireTime", "openTime", "realDeal"};

        ExcelEntity<CityDB> excelEntity = new ExcelEntity<>("", columnNames, methodNames, cityDBS);
        excelEntity.setSheetName("城市DB详情");
        Workbook excel = ExcelExporter.export2003Excel(excelEntity);
        OutputStream ouputStream = new FileOutputStream(new File(basePath + "out.xls"));
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
            List<Object> rowValue = Lists.newArrayListWithCapacity(7);
            for (Cell cell : row) {
                Object re = getCellValue(cell, cell.getCellType());
                rowValue.add(re);
            }
            CityDB db = CityDB.builder()
                    .id(new BigDecimal(String.valueOf(rowValue.get(0))).longValue())
                    .cityName((String) rowValue.get(1))
                    .communityId(getLong(rowValue.get(2)))
                    .communityName((String) rowValue.get(3))
                    .firstOpenTime(getInt(rowValue.get(4)))
                    .headMan((String) rowValue.get(5))
                    .position((String) rowValue.get(6))
                    .hireTime(getInt(rowValue.get(7)))
                    .openTime(getInt(rowValue.get(8)))
                    .realDeal(getDouble(rowValue.get(9)))
                    .build();
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
            List<Object> rowValue = Lists.newArrayListWithCapacity(7);
            for (Cell cell : row) {
                Object re = getCellValue(cell, cell.getCellType());
                rowValue.add(re);
            }
            DealAmount amount = new DealAmount(rowValue.get(0), rowValue.get(1), rowValue.get(2), rowValue.get(3),
                    rowValue.get(4), rowValue.get(5), rowValue.get(6));
            amounts.add(amount);
        }
        return amounts;
    }


    public static Object getCellValue(Cell cell, CellType cellType) {
        switch (cellType) {
            case STRING:
                return cell.getRichStringCellValue().getString().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DateUtils.dateFormat.format(cell.getDateCellValue());
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return getCellValue(cell, cell.getCachedFormulaResultType());
            case BLANK:
                return "";
            default:
                return null;
        }
    }

    @Data
    public static class DealAmount {
        private String stockName;
        private Long communityId;
        private String communityName;
        private String headMan;
        private Long dealAmt;
        private Integer date;
        private Double realDeal;

        public DealAmount(Object stockName, Object communityId, Object communityName, Object headMan, Object dealAmt, Object date, Object realDeal) {
            this.stockName = (String) stockName;
            this.communityId = getLong(communityId);
            this.communityName = (String) communityName;
            this.headMan = String.valueOf(headMan);
            this.dealAmt = getLong(dealAmt);
            this.date = getInt(date);
            this.realDeal = (Double) realDeal;
        }
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

        try {
            return new BigDecimal(String.valueOf(o).trim().replaceAll("[-/]", "")).intValue();
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


    @Data
    @Builder
    public static class CityDB {
        private Long id;
        private String cityName;
        private Long communityId;
        private String communityName;
        private Integer firstOpenTime;
        private String headMan;
        private String position;
        private Integer hireTime;
        private Integer openTime;
        private Double realDeal;

        private Integer useTime;
    }
}
