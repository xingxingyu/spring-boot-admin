package com.test.web.controller;


import com.test.mysql.entity.Userlog;
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
@RequestMapping("/userlog")
public class UserLogController {
    private Logger logger = LoggerFactory.getLogger(UserLogController.class);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date start = DateUtil.getTime(-1, 0, 0, 0);
    Date end = DateUtil.getTime(0, 0, 0, 0);
    @Autowired
    private UserLogRepository userLogRepository;
    @Autowired
    private RoleManager roleManager;

    @RequestMapping("/index")
    public String userlog(Model model, Principal user) throws Exception {
        roleManager.giveAuthority(model, user);
        return "userlog/index";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Userlog> getList(UserlogQo userlogQo) {
        try {
            Pageable pageable = new PageRequest(userlogQo.getPage(), userlogQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
            if (userlogQo.getStart() != "" && userlogQo.getStart() != null) {
                start = format.parse(userlogQo.getStart());
            }
            if (userlogQo.getEnd() != "" && userlogQo.getEnd() != null) {
                end = format.parse(userlogQo.getEnd());
            }
            return userLogRepository.findByName(start, end, pageable);
        } catch (Exception e) {
            logger.error("日期格式不对");
        }
        return null;
    }

}
