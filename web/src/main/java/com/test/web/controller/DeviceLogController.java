package com.test.web.controller;


import com.test.mysql.entity.Devicelog;
import com.test.mysql.entity.Userlog;
import com.test.mysql.model.DevicelogQo;
import com.test.mysql.model.UserlogQo;
import com.test.mysql.repository.DeviceLogRepository;
import com.test.mysql.repository.UserLogRepository;
import com.test.web.Utils.DateUtil;
import com.test.web.config.CustomSecurityMetadataSource;
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
    @RequestMapping("/index")
    public String userlog(ModelMap model, Principal user) throws Exception{
        Authentication authentication = (Authentication) user;
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
        model.addAttribute("newrole", newrole);
        model.addAttribute("editrole", editrole);
        model.addAttribute("deleterole", deleterole);

        model.addAttribute("user", user);
        logger.info("设备日志页面被访问到");
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
