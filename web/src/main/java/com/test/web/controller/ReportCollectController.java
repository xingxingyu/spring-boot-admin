package com.test.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.mysql.entity.*;
import com.test.mysql.model.ElectronicDataForReportQo;
import com.test.mysql.repository.DepartmentRepository;
import com.test.mysql.repository.ElectronicDataForReportRepository;
import com.test.mysql.repository.ReportCollectRepository;
import com.test.web.Utils.DateUtil;
import com.test.web.config.CustomSecurityMetadataSource;
import java.text.ParseException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dongmingjun on 2017/5/15.
 */
@Controller
@RequestMapping("/reportCollect")
public class ReportCollectController {
    private static Logger logger = LoggerFactory.getLogger(ReportCollectController.class);
    private SimpleDateFormat formatFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ReportCollectRepository reportCollectRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ElectronicDataForReportRepository reportRepository;

    @RequestMapping("/index")
    public String index(ModelMap model, Principal user) throws Exception {
        Authentication authentication = (Authentication)user;
        List<String> userroles = new ArrayList<String>();
        for (GrantedAuthority ga : authentication.getAuthorities()) {
            userroles.add(ga.getAuthority());
        }

        boolean newrole = false, editrole = false, deleterole = false;
        for (String key : CustomSecurityMetadataSource.resourceMap.keySet()) {
            if (key.contains("new")) {
                for (ConfigAttribute ca : CustomSecurityMetadataSource.resourceMap.get(key)) {
                    if (userroles.contains(ca.getAttribute())) {
                        newrole = true;
                        break;
                    }
                }

            }
            if (key.contains("edit")) {
                for (ConfigAttribute ca : CustomSecurityMetadataSource.resourceMap.get(key)) {
                    if (userroles.contains(ca.getAttribute())) {
                        editrole = true;
                        break;
                    }
                }

            }
            if (key.contains("delete")) {
                for (ConfigAttribute ca : CustomSecurityMetadataSource.resourceMap.get(key)) {
                    if (userroles.contains(ca.getAttribute())) {
                        deleterole = true;
                        break;
                    }
                }

            }
        }

        model.addAttribute("newrole", newrole);
        model.addAttribute("editrole", editrole);
        model.addAttribute("deleterole", deleterole);

        model.addAttribute("user", user);
        logger.info("汇总报表页面被访问到");
        return "reportCollect/index";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<F_garbageCollect> getList(ElectronicDataForReportQo electronicDataForReportQo) {
        Pageable pageable = new PageRequest(electronicDataForReportQo.getPage(), electronicDataForReportQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
        Date start = DateUtil.getTime(-1, 0, 0, 0);
        Date end = DateUtil.getTime(0, 0, 0, 0);
        Page<F_garbageCollect> list = null;
        try {
            if (electronicDataForReportQo.getStart() == null || electronicDataForReportQo.getStart() == "") {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, -30);
                start = cal.getTime();
            } else {
                start = formatFull.parse(electronicDataForReportQo.getStart() + ":00");
            }

            if (electronicDataForReportQo.getEnd() == null || electronicDataForReportQo.getEnd() == "") {
                end = new Date();
            } else {
                end = formatFull.parse(electronicDataForReportQo.getEnd() + ":59");
            }
            list = reportCollectRepository.findByTime(start, end, pageable);
            return list;
        } catch (Exception e) {
            logger.error("解析日期出现错误", e);

        }
        return null;
    }

    @RequestMapping("/view")
    public String view(ModelMap model, ElectronicDataForReportQo electronicDataForReportQo) {
        return "reportCollect/view";

    }

    public List<F_garbageCollect> getAllList(ElectronicDataForReportQo electronicDataForReportQo) {
        Pageable pageable = new PageRequest(electronicDataForReportQo.getPage(), electronicDataForReportQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
        List<F_garbageCollect> list = new ArrayList<F_garbageCollect>();
        Date start = DateUtil.getTime(-1, 0, 0, 0);
        Date end = DateUtil.getTime(0, 0, 0, 0);
        try {
            if (electronicDataForReportQo.getStart() == null || electronicDataForReportQo.getStart() == "") {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, -30);
                start = cal.getTime();
            } else {
                start = formatFull.parse(electronicDataForReportQo.getStart() + ":00");
            }

            if (electronicDataForReportQo.getEnd() == null || electronicDataForReportQo.getEnd() == "") {
                end = new Date();
            } else {
                end = formatFull.parse(electronicDataForReportQo.getEnd() + ":59");
            }
            list = reportCollectRepository.findAll(start, end);
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage() + "解析日期出现错误");

        }
        return null;
    }

    @RequestMapping(value = "/department")
    @ResponseBody
    public JSONArray viewDepartment(ElectronicDataForReportQo electronicDataForReportQo) {
        //获取部门列表
        List<Department> departments = departmentRepository.findAll();
        JSONArray array = new JSONArray();

        for (int i = 0, len = departments.size(); i < len; i++) {
            if (!"it".equalsIgnoreCase(departments.get(i).getName())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("depar", departments.get(i).getName());
                array.add(jsonObject);
            }
        }
        return array;
    }

    @RequestMapping(value = "/netweight")
    @ResponseBody
    public JSONArray viewNetWeight(ElectronicDataForReportQo electronicDataForReportQo) {

        Iterator<F_garbageCollect> netWeightList = this.getAllList(electronicDataForReportQo).iterator();
        JSONArray array = new JSONArray();
        F_garbageCollect fg = new F_garbageCollect();
        while (netWeightList.hasNext()) {
            fg = netWeightList.next();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("categoryName", fg.getCategoryName());
            jsonObject.put("netWeight", fg.getNetWeight());
            jsonObject.put("depar", fg.getDepartment());
            array.add(jsonObject);
        }
        return array;
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportExcel")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        ElectronicDataForReportQo electronicDataForReportQo = new ElectronicDataForReportQo();
        electronicDataForReportQo.setStart(request.getParameter("start"));
        electronicDataForReportQo.setEnd(request.getParameter("end"));
        //拿到净重数据
        List<F_garbageCollect> netWeightList = this.getAllList(electronicDataForReportQo);
        //获取部门列表
        List<Department> departments = departmentRepository.findAll();
        //构建部门和操作员map
        Map<String, String> deptOperaMap = buildDepart2OperatorMap(electronicDataForReportQo);
        //构建运输人员map
        Map<String, String> transMap = buildDepart2TransMap(electronicDataForReportQo);
        //构建部门和护士map
        Map<String, String> nurseMap = buildDepart2NurseMap(electronicDataForReportQo);

        //获取垃圾类型
        String[] garbageType = new String[] {"感染性废物", "病理性废物", "损伤性废物", "药物性废物", "化学性废物", "其他废物"};
        //创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        String sheetName = "医疗废物院内交接登记表";

        //单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        HSSFSheet sheet1 = workbook.createSheet(sheetName);
        HSSFCell cell = null;
        HSSFRow row = null;
        //创建头部表名称
        CellRangeAddress cra = new CellRangeAddress(0, 1, 0, 11 + 2 * garbageType.length);
        sheet1.addMergedRegion(cra);
        row = sheet1.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("医疗废物院内交接登记表");
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        //添加日期栏
        cra = new CellRangeAddress(2, 2, 0, 11 + 2 * garbageType.length);
        sheet1.addMergedRegion(cra);
        row = sheet1.createRow(2);
        cell = row.createCell(0);
        String now = formatFull.format(new Date());
        cell.setCellValue("日期  年：" + now.substring(0, 4) + " 月：" + now.substring(5, 7) + " 日：" + now.substring(8, 10));

        //添加项目科室
        row = sheet1.createRow(3);
        for (int i = 0; i <= 6 + garbageType.length; i += (6 + garbageType.length)) {
            sheet1.addMergedRegion(new CellRangeAddress(3, 4, i, i));
            cell = row.createCell(i);
            cell.setCellValue("科室\\项目");
        }

        //添加废物标识
        for (int i = 1; i <= (7 + garbageType.length); i += (6 + garbageType.length)) {
            sheet1.addMergedRegion(new CellRangeAddress(3, 3, i, i + garbageType.length - 1));
            cell = row.createCell(i);
            cell.setCellValue("医疗废物种类,重量(KG)");
        }

        //添加废物名称
        row = sheet1.createRow(4);
        for (short i = 1; i < garbageType.length + 1; i++) {
            for (short j = 0; j <= (7 + garbageType.length); j += (6 + garbageType.length)) {
                cell = row.createCell(i + j);
                cell.setCellValue(garbageType[i - 1]);
            }
        }

        //创建时间签名栏
        //       String[] signs = new String[] {"交接时间", "操作员签名", "专职运送签名", "医疗废物最终去向"};
        String[] signs = new String[] {"交接时间", "操作员签名", "运输人员", "护士签名", "医疗废物最终去向"};

        row = sheet1.getRow(3);
        for (short i = 0; i < signs.length; i++) {
            for (short j = 0; j <= (6 + garbageType.length); j += (6 + garbageType.length)) {
                cra = new CellRangeAddress(3, 4, i + j + 1 + garbageType.length, i + j + 1 + garbageType.length);
                sheet1.addMergedRegion(cra);
                cell = row.createCell(i + j + 1 + garbageType.length);
                cell.setCellValue(signs[i].toString());
            }
        }

        //左边数据条数
        int leftResordes = (int)Math.ceil((double)departments.size() / (double)2);

        //存入左边数据
        for (short i = 5; i < leftResordes + 5; i++) {   //行索引
            //创建第i行
            row = sheet1.createRow(i);
            for (short j = 0; j < garbageType.length + 1; j++) {//列索引
                cell = row.createCell(j);
                if (j <= 0) {
                    //第一列为部门
                    cell.setCellValue(departments.get(i - 5).getName());
                } else {
                    //其它列为净重
                    cell.setCellValue(this.getBydeptAndGarbageType(netWeightList, departments.get(i - 5).getName(), garbageType[j - 1].toString()));
                }
            }
        }

        //存入右边数据
        for (int i = (5 + leftResordes); i < departments.size() + 5; i++) {   //行索引
            row = sheet1.getRow(i - leftResordes);
            for (short j = 0; j < garbageType.length + 1; j++) {//列索引
                cell = row.createCell(j + 6 + garbageType.length);
                if (j <= 0) {
                    //第一列为部门
                    cell.setCellValue(departments.get(i - 5).getName());
                } else {
                    //其它列为净重
                    cell.setCellValue(this.getBydeptAndGarbageType(netWeightList, departments.get(i - 5).getName(), garbageType[j - 1].toString()));
                }
            }
        }

        //交接时间填充
        for (int i = 0; i <= 6 + garbageType.length; i += (garbageType.length + 6)) {
            for (int j = 1; j <= leftResordes; j++) {
                row = sheet1.getRow(4 + j) == null ? sheet1.createRow(4 + j) : sheet1.getRow(4 + j);
                cell = row.createCell(i + garbageType.length + 1);
                cell.setCellValue(now);
            }
        }

        //操作员填充
        for (int i = 0; i <= 6 + garbageType.length; i += (garbageType.length + 6)) {
            for (int j = 1; j <= leftResordes; j++) {
                row = sheet1.getRow(4 + j) == null ? sheet1.createRow(4 + j) : sheet1.getRow(4 + j);
                cell = row.createCell(i + garbageType.length + 2);
                String value = null;
                if (i == 0) {
                    value = row.getCell(0) != null ? deptOperaMap.get(row.getCell(0).getStringCellValue()) : null;

                } else {
                    value = row.getCell(6 + garbageType.length) != null ? deptOperaMap.get(row.getCell(6 + garbageType.length).getStringCellValue()) : null;
                }
                cell.setCellValue(value == null ? "" : value);

            }
        }

        //运输人员
        //todo
        for (int i = 0; i <= 6 + garbageType.length; i += (garbageType.length + 6)) {
            for (int j = 1; j <= leftResordes; j++) {
                row = sheet1.getRow(4 + j) == null ? sheet1.createRow(4 + j) : sheet1.getRow(4 + j);
                cell = row.createCell(i + garbageType.length + 3);
                String value = null;
                if (i == 0) {
                    value = row.getCell(0) != null ? transMap.get(row.getCell(0).getStringCellValue()) : null;

                } else {
                    value = row.getCell(6 + garbageType.length) != null ? transMap.get(row.getCell(6 + garbageType.length).getStringCellValue()) : null;
                }
                cell.setCellValue(value == null ? "" : value);

            }
        }
        //护士签名
        //todo
        for (int i = 0; i <= 6 + garbageType.length; i += (garbageType.length + 6)) {
            for (int j = 1; j <= leftResordes; j++) {
                row = sheet1.getRow(4 + j) == null ? sheet1.createRow(4 + j) : sheet1.getRow(4 + j);
                cell = row.createCell(i + garbageType.length + 4);
                String value = null;
                if (i == 0) {
                    value = row.getCell(0) != null ? nurseMap.get(row.getCell(0).getStringCellValue()) : null;

                } else {
                    value = row.getCell(5 + garbageType.length) != null ? deptOperaMap.get(row.getCell(5 + garbageType.length).getStringCellValue()) : null;
                }
                cell.setCellValue(value == null ? "" : value);

            }
        }
        //废物去向说明
        for (short i = 0; i <= (6 + garbageType.length); i += (6 + garbageType.length)) {
            cra = new CellRangeAddress(5, 5 + leftResordes, 5 + garbageType.length + i, 5 + garbageType.length + i);
            sheet1.addMergedRegion(cra);

            row = sheet1.getRow(5) == null ? sheet1.createRow(5) : sheet1.getRow(5);
            cell = row.createCell(5 + garbageType.length + i);
            cell.setCellValue("无锡市工业医疗安全处置有限公司");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            logger.error("excel写入失败");
        }

        HttpHeaders headers = new HttpHeaders();
        String fileName = new String("医疗废物院内交接登记表.xls".getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> filebyte = new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.CREATED);
        try {
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return filebyte;
    }

    //通过科室和垃圾类型查询净重
    private Double getBydeptAndGarbageType(List<F_garbageCollect> f, String dept, String type) {
        F_garbageCollect fc;
        Iterator<F_garbageCollect> it = f.iterator();
        //dept传值没有错
        while (it.hasNext()) {
            fc = it.next();
            if (StringUtils.isEmpty(fc.getDepartment()) && StringUtils.isEmpty(dept.trim())
                && fc.getDepartment().trim().equals(dept.trim())
                && fc.getCategoryName().trim().equals(type.trim())) {
                return fc.getNetWeight();
            }

        }
        return 0.0;
    }

    public Map<String, String> buildDepart2OperatorMap(
        ElectronicDataForReportQo electronicDataForReportQo) throws ParseException {

        Date start = DateUtil.getTime(-1, 0, 0, 0);
        Date end = DateUtil.getTime(0, 0, 0, 0);
        if (electronicDataForReportQo.getStart() == null || electronicDataForReportQo.getStart() == "") {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -30);
            start = cal.getTime();
        } else {
            start = formatFull.parse(electronicDataForReportQo.getStart() + ":00");
        }

        if (electronicDataForReportQo.getEnd() == null || electronicDataForReportQo.getEnd() == "") {
            end = new Date();
        } else {
            end = formatFull.parse(electronicDataForReportQo.getEnd() + ":59");
        }

        List<Object[]> list = reportRepository.findBy2Fields(start, end);
        Map<String, String> map = buildMap(list);
        return map;

    }

    public Map<String, String> buildDepart2NurseMap(
        ElectronicDataForReportQo electronicDataForReportQo) throws ParseException {

        Date start = DateUtil.getTime(-1, 0, 0, 0);
        Date end = DateUtil.getTime(0, 0, 0, 0);
        if (electronicDataForReportQo.getStart() == null || electronicDataForReportQo.getStart() == "") {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -30);
            start = cal.getTime();
        } else {
            start = formatFull.parse(electronicDataForReportQo.getStart() + ":00");
        }

        if (electronicDataForReportQo.getEnd() == null || electronicDataForReportQo.getEnd() == "") {
            end = new Date();
        } else {
            end = formatFull.parse(electronicDataForReportQo.getEnd() + ":59");
        }

        List<Object[]> list = reportRepository.findDistinctNurse(start, end);
        Map<String, String> map = buildMap(list);
        return map;

    }

    public Map<String, String> buildDepart2TransMap(
        ElectronicDataForReportQo electronicDataForReportQo) throws ParseException {

        Date start = DateUtil.getTime(-1, 0, 0, 0);
        Date end = DateUtil.getTime(0, 0, 0, 0);
        if (electronicDataForReportQo.getStart() == null || electronicDataForReportQo.getStart() == "") {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -30);
            start = cal.getTime();
        } else {
            start = formatFull.parse(electronicDataForReportQo.getStart() + ":00");
        }

        if (electronicDataForReportQo.getEnd() == null || electronicDataForReportQo.getEnd() == "") {
            end = new Date();
        } else {
            end = formatFull.parse(electronicDataForReportQo.getEnd() + ":59");
        }

        List<Object[]> list = reportRepository.findDistinctTrans(start, end);
        Map<String, String> map = buildMap(list);
        return map;

    }

    public Map<String, String> buildMap(List<Object[]> list) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Object[] m = list.get(i);

            String key = m[0] == null ? "" : m[0].toString();
            String v = m[1] == null ? "" : m[1].toString();
            if (map.get(key) == null) {
                map.put(key, v);
            } else {
                String value = !StringUtils.isEmpty(v) ? map.get(key) + ',' + v : map.get(key);
                map.put(key, value);
            }
        }
        return map;

    }

}


