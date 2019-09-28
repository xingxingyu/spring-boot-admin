package com.test.web.controller;

import com.test.mysql.entity.Role;
import com.test.mysql.entity.Userlog;
import com.test.mysql.model.RoleQo;
import com.test.mysql.repository.RoleRepository;
import com.test.mysql.repository.UserLogRepository;
import com.test.web.Utils.LogType;
import com.test.web.Utils.ResultType;
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
@RequestMapping("/role")
public class RoleController {
    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserLogRepository userLogRepository;
    @Autowired
    private RoleManager roleManager;

    @RequestMapping("/index")
    public String index(Model model, Principal user) throws Exception {
        roleManager.giveAuthority(model, user);
        return "role/index";
    }

    @RequestMapping(value = "/{id}")
    public String show(ModelMap model, @PathVariable Long id) {
        Role role = roleRepository.findOne(id);
        model.addAttribute("role", role);
        return "role/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Role> getList(RoleQo roleQo) {
        try {
            Pageable pageable = new PageRequest(roleQo.getPage(), roleQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
            return roleRepository.findByName(roleQo.getName() == null ? "%" : "%" + roleQo.getName() + "%", pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create() {
        return "role/new";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Role role, Principal user) throws Exception {
        roleRepository.save(role);
        logger.info("新增->ID=" + role.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.ADD_ROLE, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value = "/edit/{id}")
    public String update(ModelMap model, @PathVariable Long id) {
        Role role = roleRepository.findOne(id);
        model.addAttribute("role", role);
        return "role/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    @ResponseBody
    public String update(Role role, Principal user) throws Exception {
        roleRepository.save(role);
        logger.info("修改->ID=" + role.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.UPDATE_ROLE, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id, Principal user) throws Exception {
        roleRepository.delete(id);
        logger.info("删除->ID=" + id);
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.DELETE_ROLE, ResultType.SUCCESS
        ));
        return "1";
    }

}
