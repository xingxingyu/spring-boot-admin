package com.test.web.controller;

import com.test.mysql.entity.Garbage;
import com.test.mysql.entity.User;
import com.test.mysql.model.GabageDetailQo;
import com.test.mysql.repository.DepartmentRepository;
import com.test.mysql.repository.GarbageDetailRepository;
import com.test.mysql.repository.UserRepository;
import com.test.web.service.security.RoleManager;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private GarbageDetailRepository garbageDetailRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleManager roleManager;

    @RequestMapping("/index")
    public String index(ModelMap model, Principal principal) throws Exception {
        roleManager.giveAuthority((Model) model, principal);
        List<String> category = garbageDetailRepository.findCategory();
        model.addAttribute("category", category);
        return "report/index";
    }


    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Garbage> getList(GabageDetailQo gabageDetailQo) {
        try {
            Pageable pageable = new PageRequest(gabageDetailQo.getPage(), gabageDetailQo.getSize(), new Sort(Sort.Direction.DESC, "id"));
            Date start;
            Date end;

            if (gabageDetailQo.getStart() == null || gabageDetailQo.getStart() == "") {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, -30);
                start = cal.getTime();
            } else {
                out.print(gabageDetailQo.getStart() + "开始..........................");
                start = formatFull.parse(gabageDetailQo.getStart() + ":00");
            }


            if (gabageDetailQo.getEnd() == null || gabageDetailQo.getEnd() == "") {
                end = new Date();
            } else {
                out.print(gabageDetailQo.getEnd() + "结束..........................");
                end = formatFull.parse(gabageDetailQo.getEnd() + ":59");
            }

            Page<Garbage> by2Fields = garbageDetailRepository
                    .findBy2Fields(
                            gabageDetailQo.getCategoryName() == null || "".equals(gabageDetailQo.getCategoryName()) ? "%" : "%" + gabageDetailQo.getCategoryName() + "%",
                            gabageDetailQo.getDepartment() == null || "".equals(gabageDetailQo.getDepartment()) ? "%" : "%" + gabageDetailQo.getDepartment() + "%",
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
        GabageDetailQo gabageDetailQo = new GabageDetailQo();
        gabageDetailQo.setStart(request.getParameter("start"));
        gabageDetailQo.setEnd(request.getParameter("end"));
        gabageDetailQo.setCategoryName(request.getParameter("categoryName"));
        gabageDetailQo.setDepartment(request.getParameter("department"));
        String[] head = new String[]{"病区", "垃圾类型", "重量", "操作员", "称重时间", "护士", "运输人员", "运输时间", "抽检人", "抽检时间", "装车人", "装车时间"};

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
        List<Garbage> list = this.getAllList(gabageDetailQo);

        //存入数据
        for (short i = 0; i < list.size(); i++) {   //行索引
            //创建第i行
            row = sheet1.createRow(i + 1);
            for (short j = 0; j < head.length + 1; j++) {//列索引
                cell = row.createCell(j);
                if (j == 0) {
                    cell.setCellValue(list.get(i).getDepartment());
                } else if (j == 1) {
                    cell.setCellValue(list.get(i).getCategoryName() == null ? "" : list.get(i).getCategoryName());
                } else if (j == 2) {
                    cell.setCellValue(list.get(i).getNetWeight() == null ? 0 : list.get(i).getNetWeight());
                } else if (j == 3) {
                    cell.setCellValue(list.get(i).getOperator() == null ? "" : list.get(i).getOperator());
                } else if (j == 4) {
                    cell.setCellValue(list.get(i).getUp_Date() == null ? format.format(new Date()) : format.format(list.get(i).getUp_Date()));
                } else if (j == 5) {
                    cell.setCellValue(list.get(i).getNurseName() == null ? "" : list.get(i).getNurseName());
                } else if (j == 6) {
                    cell.setCellValue(list.get(i).getTransName() == null ? "" : list.get(i).getTransName());
                } else if (j == 7) {
                    cell.setCellValue(list.get(i).getMtime2() == null ? format.format(new Date()) : format.format(list.get(i).getMtime2()));
                } else if (j == 8) {
                    cell.setCellValue(list.get(i).getScheckp() == null ? "" : list.get(i).getScheckp());
                } else if (j == 9) {
                    cell.setCellValue(list.get(i).getSchecpt() == null ? format.format(new Date()) : format.format(list.get(i).getSchecpt()));
                } else if (j == 10) {
                    cell.setCellValue(list.get(i).getTransitp() == null ? "" : list.get(i).getTransitp());
                } else if (j == 11) {
                    cell.setCellValue(list.get(i).getTransitpt() == null ? format.format(new Date()) : format.format(list.get(i).getTransitpt()));
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
            logger.error("服务器内部错误", e);
        }
        return filebyte;
    }


    public List<Garbage> getAllList(GabageDetailQo gabageDetailQo) {
        Pageable pageable = new PageRequest(gabageDetailQo.getPage(), gabageDetailQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
        List<Garbage> list = new ArrayList<>();
        SimpleDateFormat formatFull = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date start;
        Date end;
        try {
            if (gabageDetailQo.getStart() == null || gabageDetailQo.getStart() == "") {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, -1);
                start = cal.getTime();
            } else {
                out.print(gabageDetailQo.getStart() + "开始..........................");
                start = formatFull.parse(gabageDetailQo.getStart() + ":00");
            }


            if (gabageDetailQo.getEnd() == null || gabageDetailQo.getEnd() == "") {
                end = new Date();
            } else {
                out.print(gabageDetailQo.getEnd() + "结束..........................");
                end = formatFull.parse(gabageDetailQo.getEnd() + ":59");
            }
            list = garbageDetailRepository.findBy2Fields(
                    gabageDetailQo.getCategoryName() == null ? "%" : "%" + gabageDetailQo.getCategoryName() + "%",
                    gabageDetailQo.getDepartment() == null ? "%" : "%" + gabageDetailQo.getDepartment() + "%",
                    start, end);
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage() + "解析日期出现错误");

        }
        return null;
    }

}
