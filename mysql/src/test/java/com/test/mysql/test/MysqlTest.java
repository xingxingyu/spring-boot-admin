package com.test.mysql.test;

import com.test.mysql.entity.*;
import com.test.mysql.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfiguration.class})
public class MysqlTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserLogRepository userLogRepository;
    @Autowired
    DeviceLogRepository deviceLogRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    ResourceRepository resourceRepository;
    @Before
    public void initData(){
        //清空用户，角色，部门表
        userRepository.deleteAll();
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
        //初始化resource
        Resource resource1 = new Resource("/**/new/**");
        Resource resource2 = new Resource("/**/edit/**");
        Resource resource3 = new Resource("/**/delete/**");
        resourceRepository.save(resource1);
        resourceRepository.save(resource2);
        resourceRepository.save(resource3);

        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(resource1);
        resourceList.add(resource2);
        resourceList.add(resource3);

        //初始化Authority
        Authority authority = new Authority("系統权限", resourceList);
        authorityRepository.save(authority);

        //创建IT部门并且保存到表
        Department department = new Department();
        department.setName("IT");
        departmentRepository.save(department);
        Assert.notNull(department.getId());

        //创建admin用户并且保存到表
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        Role role = new Role();
        role.setName("admin");
        role.setAuthorities(authorities);
        role.setCreatedate(new Date());
        roleRepository.save(role);
        Assert.notNull(role.getId());

        //创建用户信息并且保存
        User user = new User();
        user.setName("admin");
        user.setRoles(roleRepository.findByName("admin"));
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        user.setPassword(bpe.encode("admin"));
        user.setCreatedate(new Date());
        user.setDepartment(department);
        userRepository.save(user);
        Assert.notNull(user.getId());

        //创建用户日志信息并且保存
        Userlog userlog = new Userlog();
        userlog.setName("admin");
        userlog.setAction("登录");
        userlog.setCreatedate(new Date());
        userLogRepository.save(userlog);
        Assert.notNull(userlog.getId());

        //创建设备日志信息并且保存
        Devicelog devicelog = new Devicelog();
        devicelog.setDevice_id("001");
        devicelog.setCreatedate(new Date());
        devicelog.setAction("正在运行");
        deviceLogRepository.save(devicelog);
        Assert.notNull(devicelog.getId());
        
        //创建设备日志信息并且保存
        Device dev = new Device();
        dev.setNum("sp000001");
        dev.setDepartments("皮肤科");
        dev.setIp("192.168.1.200");
        dev.setPort("33581");
        dev.setExamine("无误差");
        dev.setStat("正在运行");
        deviceRepository.save(dev);
        Assert.notNull(dev.getId());
    
    }
    @Test
    public void insertUserRoles(){
/*        User user = userRepository.findByName("admin");
        Assert.notNull(user);

        List<Role> roles = roleRepository.findAll();
        Assert.notNull(roles);
        user.setRoles(roles);
        System.out.print("-----------------" +  user.getName()  + "-----------------");
        System.out.print(user.toString());
        System.out.print("-----------------" +  user.getName()  + "-----------------");
        userRepository.save(user);*/
    }
}
