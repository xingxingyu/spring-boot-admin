package com.test.web.controller;

import com.test.mysql.entity.*;
import com.test.mysql.model.ResourceQo;
import com.test.mysql.model.UserQo;
import com.test.mysql.repository.*;
import com.test.web.Utils.LogType;
import com.test.web.Utils.ResultType;
import com.test.web.Utils.Test;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;

@Controller
@RequestMapping("/resource")
public class ResourceController {
    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserLogRepository userLogRepository;
    @Autowired
    private ResourceRepository resourceRepository;


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
        return "resource/index";
    }


    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Resource> getList(ResourceQo resourceQo) {
        try {
            logger.info("开始获取resourse 00000000");
            Pageable pageable = new PageRequest(resourceQo.getPage(), resourceQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
            return resourceRepository.findResourceByResourceNameLike(resourceQo.getResourceName() == null ? "%" : "%" + resourceQo.getResourceName() + "%", pageable);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(ModelMap model, Resource resource, Principal user) {
        resource.setCreatedBy(user.getName());
        resource.setUpdatedBy(user.getName());
        resource.setCreateTime(new Date());
        resource.setUpdateTime(new Date());
        return "resource/new";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(User user) throws Exception {
        user.setCreatedate(new Date());
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        user.setPassword(bpe.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("新增->ID=" + user.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.ADD_USER, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value = "/edit/{id}")
    public String edit(ModelMap model, @PathVariable Long id,Principal principal) {
        Resource resource = resourceRepository.findById(id);


        model.addAttribute("resource", resource);

        return "resource/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    @ResponseBody
    public String update(Resource resource, Principal user) throws Exception {
        resource.setUpdateTime(new Date());
        resource.setUpdatedBy(user.getName());
        resourceRepository.save(resource);
        logger.info("修改->ID=" + resource.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.UPDATE_RESOURCE, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id, Principal user) throws Exception {
        resourceRepository.delete(id);
        logger.info("删除->ID=" + id);
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.DELETE_RESOURCE, ResultType.SUCCESS
        ));
        return "1";
    }

}
