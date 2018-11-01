package com.test.web.controller;

import com.test.mysql.entity.Department;
import com.test.mysql.entity.F_garbage;
import com.test.mysql.entity.F_garbageCollect;
import com.test.mysql.entity.User;
import com.test.mysql.model.ElectronicDataForReportQo;
import com.test.mysql.repository.DepartmentRepository;
import com.test.mysql.repository.ElectronicDataForReportRepository;
import com.test.mysql.repository.UserRepository;
import com.test.web.Utils.DateUtil;
import com.test.web.config.CustomSecurityMetadataSource;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by dongmingjun on 2017/5/15.
 */
@Controller
@RequestMapping("/report")
public class ReportController {
    private static Logger logger = LoggerFactory.getLogger(ReportController.class);
    private SimpleDateFormat formatFull = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    @Autowired
    private ElectronicDataForReportRepository electronicDataForReportRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/index")
    public String index(ModelMap model, Principal principal) throws Exception {
        Authentication authentication = (Authentication) principal;
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

        out.print("new role is" + newrole + "editrole is " + editrole + "deleterole is " + deleterole);
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("newrole", newrole);
        model.addAttribute("editrole", editrole);
        model.addAttribute("deleterole", deleterole);
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);
        List<String> category = electronicDataForReportRepository.findCategory();
        model.addAttribute("category", category);
        return "report/index";
    }


    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<F_garbage> getList(ElectronicDataForReportQo electronicDataForReportQo) {
        try {
            Pageable pageable = new PageRequest(electronicDataForReportQo.getPage(), electronicDataForReportQo.getSize(), new Sort(Sort.Direction.DESC, "id"));
            Date start;
            Date end;

            if (electronicDataForReportQo.getStart() == null || electronicDataForReportQo.getStart() == "") {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, -30);
                start = cal.getTime();
            } else {
                out.print(electronicDataForReportQo.getStart() + "开始..........................");
                start = formatFull.parse(electronicDataForReportQo.getStart() + ":00");
            }


            if (electronicDataForReportQo.getEnd() == null || electronicDataForReportQo.getEnd() == "") {
                end = new Date();
            } else {
                out.print(electronicDataForReportQo.getEnd() + "结束..........................");
                end = formatFull.parse(electronicDataForReportQo.getEnd() + ":59");
            }

            Page<F_garbage> by2Fields = electronicDataForReportRepository
                    .findBy2Fields(
                            electronicDataForReportQo.getCategoryName() == null || "".equals(electronicDataForReportQo.getCategoryName()) ? "%" : "%" + electronicDataForReportQo.getCategoryName() + "%",
                            electronicDataForReportQo.getDepartment() == null || "".equals(electronicDataForReportQo.getDepartment()) ? "%" : "%" + electronicDataForReportQo.getDepartment() + "%",
                            start, end, pageable);
            return by2Fields;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

    }

    @RequestMapping("/view")
    public String view(ModelMap model, User user) {
        out.print("返回结果视图");
        return "report/view";
    }


    /**
     * 文件下载
     *
     * @param fileName
     * @param request
     * @param response
     * @return
     * @Description:
     */
    @RequestMapping("/exportExcel")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ElectronicDataForReportQo electronicDataForReportQo = new ElectronicDataForReportQo();
        electronicDataForReportQo.setStart(request.getParameter("start"));
        electronicDataForReportQo.setEnd(request.getParameter("end"));
        electronicDataForReportQo.setCategoryName(request.getParameter("categoryName"));
        electronicDataForReportQo.setDepartment(request.getParameter("department"));
        String[] head = new String[]{"ID", "物料号", "物料名称", "生产批号", "净重", "皮重", "预置皮重", "上传时间", "打印序列号", "电子秤IP", "科室", "操作员", "是否复核", "复核差异", "复核结果"};

        //创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        String sheetName = "各科室垃圾明细数据";
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        HSSFSheet sheet1 = workbook.createSheet(sheetName);
        HSSFCell cell = null;
        HSSFRow row = null;
        //添加表头
        row = sheet1.createRow(0);    //创建第一行
        out.println("第一行创建成功");
        for (short i = 0; i < head.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(head[i]);
        }

        //根据条件获取所有的称重记录
        List<F_garbage> list = this.getAllList(electronicDataForReportQo);

        //存入数据
        for (short i = 0; i < list.size(); i++) {   //行索引
            //创建第i行
            row = sheet1.createRow(i + 1);
            for (short j = 0; j < head.length + 1; j++) {//列索引
                cell = row.createCell(j);
                if (j == 0) {
                    cell.setCellValue(list.get(i).getId());
                } else if (j == 1) {
                    cell.setCellValue(list.get(i).getCategoryId() == null ? "" : list.get(i).getCategoryId());
                } else if (j == 2) {
                    cell.setCellValue(list.get(i).getCategoryName() == null ? "" : list.get(i).getCategoryName());
                } else if (j == 3) {
                    cell.setCellValue(list.get(i).getBachId() == null ? "" : list.get(i).getBachId());
                } else if (j == 4) {
                    cell.setCellValue(list.get(i).getNetWeight() == null ? 0 : list.get(i).getNetWeight());
                } else if (j == 5) {
                    cell.setCellValue(list.get(i).getTare() == null ? 0 : list.get(i).getTare());
                } else if (j == 6) {
                    cell.setCellValue(list.get(i).getPreTare() == null ? 0 : list.get(i).getPreTare());
                } else if (j == 7) {
                    cell.setCellValue(list.get(i).getUp_Date() == null ? format.format(new Date()) : format.format(list.get(i).getUp_Date()));
                } else if (j == 8) {
                    cell.setCellValue(list.get(i).getSn() == null ? -1L : list.get(i).getSn());
                } else if (j == 9) {
                    cell.setCellValue(list.get(i).getIp() == null ? "" : list.get(i).getIp());
                } else if (j == 10) {
                    cell.setCellValue(list.get(i).getDepartment() == null ? "" : list.get(i).getDepartment());
                } else if (j == 11) {
                    cell.setCellValue(list.get(i).getOperator() == null ? "" : list.get(i).getOperator());
                } else if (j == 12) {
                    cell.setCellValue(list.get(i).getIsCheck() == null ? -1 : list.get(i).getIsCheck());
                } else if (j == 13) {
                    cell.setCellValue(list.get(i).getCheckDif() == null ? -1 : list.get(i).getCheckDif());
                } else if (j == 14) {
                    cell.setCellValue(list.get(i).getCheckRs() == null ? -1 : list.get(i).getCheckRs());
                }
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {

            logger.error("excel写入失败");
        }

        HttpHeaders headers = new HttpHeaders();
        String fileName = new String("医疗废物明细表.xls".getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
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


    public List<F_garbage> getAllList(ElectronicDataForReportQo electronicDataForReportQo) {
        Pageable pageable = new PageRequest(electronicDataForReportQo.getPage(), electronicDataForReportQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
        List<F_garbage> list = new ArrayList<>();
        SimpleDateFormat formatFull = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date start;
        Date end;
        try {
            if (electronicDataForReportQo.getStart() == null || electronicDataForReportQo.getStart() == "") {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, -1);
                start = cal.getTime();
            } else {
                out.print(electronicDataForReportQo.getStart() + "开始..........................");
                start = formatFull.parse(electronicDataForReportQo.getStart() + ":00");
            }


            if (electronicDataForReportQo.getEnd() == null || electronicDataForReportQo.getEnd() == "") {
                end = new Date();
            } else {
                out.print(electronicDataForReportQo.getEnd() + "结束..........................");
                end = formatFull.parse(electronicDataForReportQo.getEnd() + ":59");
            }
            list = electronicDataForReportRepository.findBy2Fields(
                    electronicDataForReportQo.getCategoryName() == null ? "%" : "%" + electronicDataForReportQo.getCategoryName() + "%",
                    electronicDataForReportQo.getDepartment() == null ? "%" : "%" + electronicDataForReportQo.getDepartment() + "%",
                    start, end);
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage() + "解析日期出现错误");

        }
        return null;
    }

}
