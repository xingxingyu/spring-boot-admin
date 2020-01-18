package com.test.web.service;

import com.test.mysql.entity.MonthStatisc;
import com.test.mysql.repository.GarbageDetailRepository;
import com.test.mysql.repository.MonthStaticRepository;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ReoportServiceImpl implements ReoportService {
    private static final Logger logger = LoggerFactory.getLogger(ReoportServiceImpl.class);


    @Autowired
    private GarbageDetailRepository garbageDetailRepository;

    @Autowired
    private MonthStaticRepository monthStaticRepository;

    @Override
    public byte[] downloadReport1(String start, String end) {
        return new byte[0];
    }

    @Override
    public byte[] downloadReport2(String month) {
        List<MonthStatisc> monthStatiscs = listReport2(month);
        String yearstr = month.substring(0, 4);
        String monthstr = month.substring(4, 6);
        HSSFWorkbook workbook = new HSSFWorkbook();
        String sheetName = "医疗废弃物月度报表";
        //获取垃圾类型
        String[] garbageType = new String[]{"感染性废物", "病理性废物", "损伤性废物", "药物性废物", "化学性废物", "未被污染的玻璃瓶","未被污染的输液袋或瓶","胎盘","胎盘数量"};
        //单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        HSSFSheet sheet1 = workbook.createSheet(sheetName);
        HSSFCell cell = null;
        HSSFRow row = null;
        //创建头部表名称
        CellRangeAddress cra = new CellRangeAddress(0, 1, 0, 1 + garbageType.length);
        sheet1.addMergedRegion(cra);
        row = sheet1.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue(sheetName + "       年月：" + yearstr + "年" + monthstr + "月");
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);

        //添加日期类型
        row = sheet1.createRow(2);
        row.createCell(0).setCellValue("日期\\类型");
        int i = 1;
        for (String gType : garbageType) {
            row.createCell(i).setCellValue(gType);
            i++;
        }

        for (i = 1; i <= getDaysOfMonth(Integer.valueOf(yearstr), monthstr); i++) {
            row = sheet1.createRow(i + 2);
            row.createCell(0).setCellValue(i);
            for (int j = 1; j < garbageType.length + 1; j++) {
                if (j == garbageType.length) {
                    row.createCell(j).setCellValue(findPeitaiNum( i, monthStatiscs));
                } else {
                    row.createCell(j).setCellValue(findVale(garbageType[j - 1], i, monthStatiscs));
                }
            }
        }


        //合计
        row = sheet1.createRow(34);
        row.createCell(0).setCellValue("合计");
        for (int j = 1; j < garbageType.length; j++) {
            row.createCell(j).setCellValue(sumCatogory(garbageType[j - 1], monthStatiscs));
        }
        row.createCell(garbageType.length).setCellValue(sum(monthStatiscs));


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            logger.error("excel写入失败");
        }

        byte[] bytes = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            logger.warn("io errror", e);
        }

        return bytes;
    }

    @Override
    public List<MonthStatisc> listReport2(String month) {
        List<Object[]> strings = monthStaticRepository.selectMonth(month);
        List<MonthStatisc> monthStatiscs = deserialization(strings);
        Collections.sort(monthStatiscs, new Comparator<MonthStatisc>() {
            @Override
            public int compare(MonthStatisc o1, MonthStatisc o2) {
                return o1.getSdate().compareTo(o2.getSdate());
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        return monthStatiscs;
    }

    double findVale(String categoryName, int dayIndex, List<MonthStatisc> monthStatiscList) {
        String month = null;
        if (dayIndex < 10) {
            month = "0" + dayIndex;
        } else {
            month = dayIndex + "";
        }

        for (MonthStatisc monthStatisc : monthStatiscList) {
            if (monthStatisc.getSdate() != null && monthStatisc.getSdate().endsWith(month) &&
                    categoryName.equals(monthStatisc.getCategoryName())
            ) {
                return monthStatisc.getNetWeight();
            }
        }
        return 0.00;
    }

    int findPeitaiNum(int dayIndex, List<MonthStatisc> monthStatiscList) {
        String month = null;
        if (dayIndex < 10) {
            month = "0" + dayIndex;
        } else {
            month = dayIndex + "";
        }
        int sum = 0;

        for (MonthStatisc monthStatisc : monthStatiscList) {
            if (monthStatisc.getSdate() != null && monthStatisc.getSdate().endsWith(month)) {
                sum = sum + (monthStatisc.getPeitaiNum() == null ? 0 : monthStatisc.getPeitaiNum().intValue());
            }
        }
        return sum;
    }


    List<MonthStatisc> deserialization(List<Object[]> strings) {

        if (strings == null) {
            return null;
        }
        List<MonthStatisc> monthStatiscs = new ArrayList<>();

        for (Object[] array : strings) {
            MonthStatisc monthStatisc = new MonthStatisc();
            monthStatisc.setSdate(String.valueOf(array[0]));
            monthStatisc.setCategoryName(String.valueOf(array[1]));
            monthStatisc.setNetWeight(Double.valueOf(array[2].toString()));
            monthStatisc.setPeitaiNum(array[3] != null ? ((BigDecimal) array[3]).longValue() : 0);
            monthStatiscs.add(monthStatisc);
        }
        return monthStatiscs;
    }

    double sumCatogory(String categoryName, List<MonthStatisc> monthStatiscList) {
        double sum = 0.0;

        for (MonthStatisc monthStatisc : monthStatiscList) {
            if (
                    categoryName.equals(monthStatisc.getCategoryName())) {
                sum += monthStatisc.getNetWeight();
            }
        }
        return sum;
    }

    double sum(List<MonthStatisc> monthStatiscList) {
        double sum = 0.0;
        for (MonthStatisc monthStatisc : monthStatiscList) {
            sum += monthStatisc.getPeitaiNum();
        }
        return sum;
    }


    public int getDaysOfMonth(int yearStr, String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yearStr);
        calendar.set(Calendar.MONTH, Integer.valueOf(month) - 1);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

}
