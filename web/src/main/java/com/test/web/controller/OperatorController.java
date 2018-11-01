package com.test.web.controller;

import com.test.mysql.entity.*;
import com.test.mysql.model.OperatorQo;
import com.test.mysql.model.UserQo;
import com.test.mysql.repository.*;
import com.test.web.Utils.LogType;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;

@Controller
@RequestMapping("/operator")
public class OperatorController {
    private static Logger logger = LoggerFactory.getLogger(OperatorController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserLogRepository userLogRepository;
    @Autowired
    private OperatorRepository operatorRepository;



    @RequestMapping("/index")
    public String index(ModelMap model, Principal user) throws Exception{
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
        return "operator/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model,@PathVariable Long id) {
        User user = userRepository.findOne(id);
        model.addAttribute("user",user);
        return "operator/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Operator> getList(OperatorQo operatorQo) {
        try {
            Pageable pageable = new PageRequest(operatorQo.getPage(), operatorQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
         out.print("Operator:" + operatorQo.getOperator() + "Department:" + operatorQo.getDepartment());
            return operatorRepository.findAllPage(operatorQo.getOperator()==null?"%":"%"+operatorQo.getOperator()+"%",
                    operatorQo.getDepartment()==null?"%":"%"+operatorQo.getDepartment()+"%", pageable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(ModelMap model,User user){
        List<Department> departments = departmentRepository.findAll();
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("departments",departments);
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);

        return "operator/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Operator operator,Principal user) throws Exception{
        operator.setCreateTime(new Date());
        operator.setUpdateTime(new Date());
        operator.setCreatedBy(user.getName());
        operator.setUpdatedBy(user.getName());
        operatorRepository.save(operator);
        logger.info("新增->ID="+operator.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.ADD_META, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model,@PathVariable Long id){
        Operator operator = operatorRepository.findOne(id);
        model.addAttribute("operator", operator);
        return "operator/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public String update(Operator operator,Principal user) throws Exception{

        operator.setUpdateTime(new Date());
        operator.setUpdatedBy(user.getName());
        operatorRepository.save(operator);
        logger.info("修改->ID="+operator.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.UPDATE_USER, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id,Principal user) throws Exception{
        operatorRepository.delete(id);
        logger.info("删除->ID="+id);
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.DELETE_META, ResultType.SUCCESS
        ));
        return "1";
    }

}
