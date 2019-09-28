package com.test.web.service.security;

import com.test.mysql.entity.Department;
import com.test.mysql.entity.User;
import com.test.mysql.repository.DepartmentRepository;
import com.test.mysql.repository.ElectronicDataForReportRepository;
import com.test.mysql.repository.UserRepository;
import com.test.web.config.CustomSecurityMetadataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoleManager {
    private static final Logger logger = LoggerFactory.getLogger(RoleManager.class);
    @Autowired
    private ElectronicDataForReportRepository electronicDataForReportRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    public Model giveAuthority(Model model, Principal principal) {
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

        logger.info("new role is" + newrole + "editrole is " + editrole + "deleterole is " + deleterole);
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("newrole", newrole);
        model.addAttribute("editrole", editrole);
        model.addAttribute("deleterole", deleterole);
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("user", user);
        return model;
    }
}
