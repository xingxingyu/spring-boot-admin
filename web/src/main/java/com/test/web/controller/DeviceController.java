package com.test.web.controller;

import com.test.mysql.entity.*;
import com.test.mysql.model.DeviceQo;
import com.test.mysql.model.UserQo;
import com.test.mysql.repository.*;
import com.test.web.Utils.LogType;
import com.test.web.Utils.PingUtil;

import com.test.web.Utils.ResultType;
import com.test.web.config.CustomSecurityMetadataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.*;

import static java.lang.System.out;

@Controller
@RequestMapping("/device")
public class DeviceController {
    private static Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserLogRepository userLogRepository;


    @RequestMapping("/index")
    public String index(ModelMap model, Principal user) throws Exception {
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
        return "device/index";
    }


    @RequestMapping("/monitor")
    public String monitor(ModelMap model, Principal user) throws Exception {
        Authentication authentication = (Authentication) user;
        List<String> userroles = new ArrayList<String>();
        for (GrantedAuthority ga : authentication.getAuthorities()) {
            userroles.add(ga.getAuthority());
        }

/*        boolean newrole=false,editrole=false,deleterole=false;
        if(!StringUtils.isEmpty(urlroles)) {
            String[] resouces = urlroles.split(";");
            for (String resource : resouces) {
                String[] urls = resource.split("=");
                if(urls[0].indexOf("new") > 0){
                    String[] newroles = urls[1].split(",");
                    for(String str : newroles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            newrole = true;
                            break;
                        }
                    }
                }else if(urls[0].indexOf("edit") > 0){
                    String[] editoles = urls[1].split(",");
                    for(String str : editoles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            editrole = true;
                            break;
                        }
                    }
                }else if(urls[0].indexOf("delete") > 0){
                    String[] deleteroles = urls[1].split(",");
                    for(String str : deleteroles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            deleterole = true;
                            break;
                        }
                    }
                }
            }
        }

        model.addAttribute("newrole", newrole);
        model.addAttribute("editrole", editrole);
        model.addAttribute("deleterole", deleterole);*/

        model.addAttribute("user", user);
        return "device/monitor";
    }

    @RequestMapping(value = "/{id}")
    public String show(ModelMap model, @PathVariable Long id) {
        Device device = deviceRepository.findOne(id);

        model.addAttribute("device", device);
        return "device/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Device> getList(DeviceQo deviceQo) {
        try {

            Pageable pageable = new PageRequest(deviceQo.getPage(), deviceQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
            return deviceRepository.findByNum(deviceQo.getNum() == null ? "%" : "%" + deviceQo.getNum() + "%", pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(ModelMap model, User user) {
        List<Department> departments = departmentRepository.findAll();
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        return "device/new";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Device device, Principal user) throws Exception {
        deviceRepository.save(device);
        logger.info("新增->ID=" + device.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.ADD_DEVICE, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value = "/edit/{id}")
    public String update(ModelMap model, @PathVariable Long id) {

        Device device = deviceRepository.findOne(id);
        List<Department> departments = departmentRepository.findAll();
        List<Role> roles = roleRepository.findAll();

        List<Long> rids = new ArrayList<Long>();

        model.addAttribute("device", device);
        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);
        model.addAttribute("rids", rids);
        return "device/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    @ResponseBody
    public String update(Device device, Principal user) throws Exception {
        deviceRepository.save(device);
        logger.info("修改->ID=" + device.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.UPDATE_DEVICE, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id, Principal user) throws Exception {
        deviceRepository.delete(id);
        logger.info("删除->ID=" + id);
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.DELETE_DEVICE, ResultType.SUCCESS
        ));
        return "1";
    }


    @RequestMapping(value = "/checkstat", method = RequestMethod.GET)
    public String check() throws Exception {
        List list = deviceRepository.findAll();
        for (int i = 0; i < list.size(); i++) {
            Device dev = (Device) list.get(i);
            Integer countSucce = PingUtil.doPingCmd(dev.getIp(), 1);
            if (countSucce == 0) {
                dev.setStat("停止运行");
            } else {
                dev.setStat("正在运行");
            }
            deviceRepository.save(dev);
        }

        return "";
    }
}
