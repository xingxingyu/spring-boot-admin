package com.test.web.controller;


import com.test.mysql.entity.Devicelog;
import com.test.mysql.entity.Userlog;
import com.test.mysql.model.DevicelogQo;
import com.test.mysql.model.UserlogQo;
import com.test.mysql.repository.DeviceLogRepository;
import com.test.mysql.repository.UserLogRepository;
import com.test.web.Utils.DateUtil;
import com.test.web.config.CustomSecurityMetadataSource;
import com.test.web.service.security.RoleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by dylan on 2017/4/22.
 */
@Controller
@RequestMapping("/devicelog")
public class DeviceLogController {
    private Logger logger = LoggerFactory.getLogger(DeviceLogController.class);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date start = DateUtil.getTime(-1,0,0,0);
    Date end = DateUtil.getTime(0,0,0,0);
    @Autowired
    private DeviceLogRepository deviceLogRepository;
    @Autowired
    private RoleManager roleManager;

    @RequestMapping("/index")
    public String userlog(Model model, Principal user) {
        roleManager.giveAuthority(model,user);
        return "devicelog/index";
    }
    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Devicelog> getList(DevicelogQo devicelogQo) {
        try {
            Pageable pageable = new PageRequest(devicelogQo.getPage(), devicelogQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
            if(devicelogQo.getStart()!=null &&devicelogQo.getStart()!="" ){
                start=format.parse(devicelogQo.getStart());
            }
            if(devicelogQo.getEnd()!=null &&devicelogQo.getEnd()!=""){
                end=format.parse(devicelogQo.getEnd());
            }
            return deviceLogRepository.findByDeviceIdAndDate(start,end,pageable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
