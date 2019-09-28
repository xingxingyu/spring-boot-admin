package com.test.web.controller;


import com.test.mysql.entity.MonthStatisc;
import com.test.mysql.model.PageQo;
import com.test.web.service.ReoportService;
import com.test.web.service.security.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/reportMonth")
public class MonthCollectController {
    @Autowired
    private ReoportService reoportService;
    @Autowired
    private RoleManager roleManager;

    @RequestMapping("/index")
    public String index(ModelMap model, Principal principal) throws Exception {
        roleManager.giveAuthority((Model) model, principal);
        return "reportMonth/index";
    }


    @RequestMapping(value = "/list/{sdate}")
    @ResponseBody
    public Page<MonthStatisc> listReport(@PathVariable String sdate, PageQo page) {
        List<MonthStatisc> monthStatiscs = reoportService.listReport2(sdate);
        Pageable pageable = new PageRequest(page.getPage(), page.getSize(), new Sort(Sort.Direction.ASC, "sdate"));

        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > monthStatiscs.size() ? monthStatiscs.size():( start + pageable.getPageSize());

        PageImpl<MonthStatisc> monthStatiscs1 = new PageImpl(monthStatiscs.subList(start,end),pageable,monthStatiscs.size());

        return monthStatiscs1;

    }

    @RequestMapping(value = "/down/{sdate}")
    @ResponseBody
    public ResponseEntity<byte[]> downReport(@PathVariable String sdate) throws UnsupportedEncodingException {
        byte[] bytes = reoportService.downloadReport2(sdate);
        HttpHeaders headers = new HttpHeaders();
        String fileName = new String("医疗废弃物月度报表.xls".getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> filebyte = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
        return filebyte;
    }
}
