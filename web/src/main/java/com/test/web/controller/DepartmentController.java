package com.test.web.controller;

import com.test.mysql.entity.Department;
import com.test.mysql.entity.Userlog;
import com.test.mysql.model.DepartmentQo;
import com.test.mysql.repository.DepartmentRepository;
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
@RequestMapping("/department")
public class DepartmentController {
    private static Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserLogRepository userLogRepository;
    @Autowired
    private RoleManager roleManager;

    @RequestMapping("/index")
    public String index(Model model, Principal user) throws Exception{
        roleManager.giveAuthority(model,user);
        return "department/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model,@PathVariable Long id) {
        Department department = departmentRepository.findOne(id);
        model.addAttribute("department",department);
        return "department/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Page<Department> getList(DepartmentQo departmentQo) {
        try {
            Pageable pageable = new PageRequest(departmentQo.getPage(), departmentQo.getSize(), new Sort(Sort.Direction.ASC, "id"));
            return departmentRepository.findByName(departmentQo.getName()==null?"%":"%"+departmentQo.getName()+"%", pageable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/new")
    public String create(){
        return "department/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Department department,Principal user) throws Exception{
        departmentRepository.save(department);
        logger.info("新增->ID="+department.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.ADD_DEPARTMENT, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model,@PathVariable Long id){
        Department department = departmentRepository.findOne(id);
        model.addAttribute("department",department);
        return "department/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public String update(Department department,Principal user) throws Exception{
        out.print("qqqqqq");
        departmentRepository.save(department);
        logger.info("修改->ID="+department.getId());
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.UPDATE_DEPARTMENT, ResultType.SUCCESS
        ));
        return "1";
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id,Principal user) throws Exception{
        departmentRepository.delete(id);
        logger.info("删除->ID="+id);
        userLogRepository.save(new Userlog(user.getName(),
                new Date(), LogType.DELETE_DEPARTMENT, ResultType.SUCCESS
        ));
        return "1";
    }

}
