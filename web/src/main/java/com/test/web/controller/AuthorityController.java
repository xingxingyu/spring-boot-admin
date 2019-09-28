package com.test.web.controller;

import com.test.mysql.entity.*;
import com.test.mysql.model.AuthorityQo;
import com.test.mysql.model.ResourceQo;
import com.test.mysql.repository.*;
import com.test.web.Utils.LogType;
import com.test.web.Utils.ResultType;
import com.test.web.Utils.Test;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping("/authority")
public class AuthorityController {
    private static Logger logger = LoggerFactory.getLogger(AuthorityController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserLogRepository userLogRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleManager roleManager;

    @RequestMapping("/index")
    public String index(Model model, Principal user) throws Exception {
        roleManager.giveAuthority(model,user);
        return "authority/index";
    }


    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Authority> getList(AuthorityQo authorityQo) {
        try {
            Pageable pageable = new PageRequest(authorityQo.getPage(), authorityQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
            return authorityRepository.findByAuthorityNameLike(authorityQo.getAuthorityName() == null ? "%" : "%" + authorityQo.getAuthorityName() + "%", pageable);
        } catch (Exception e) {
            logger.error(e.getMessage());
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
        return "user/new";
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
    public String update(ModelMap model, @PathVariable Long id) {
        User user = userRepository.findOne(id);

        List<Department> departments = departmentRepository.findAll();
        List<Role> roles = roleRepository.findAll();

        List<Long> rids = new ArrayList<Long>();
        for (Role role : user.getRoles()) {
            rids.add(role.getId());
        }

        model.addAttribute("user", user);
        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);
        model.addAttribute("rids", rids);
        return "user/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    @ResponseBody
    public String update(Authority authority,Principal user) throws Exception {
        authorityRepository.save(authority);
        logger.info("修改->ID=" + authority.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.UPDATE_AUTHORITY, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id, Principal user) throws Exception {
        authorityRepository.delete(id);
        logger.info("删除->ID=" + id);
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.DELETE_AUTHORITY, ResultType.SUCCESS
        ));
        return "1";
    }

}
