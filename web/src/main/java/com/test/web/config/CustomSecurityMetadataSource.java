package com.test.web.config;

import com.test.mysql.entity.Authority;
import com.test.mysql.entity.Resource;
import com.test.mysql.entity.Role;
import com.test.mysql.repository.RoleRepository;
import com.test.web.Utils.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import java.util.*;

import static java.lang.System.out;

@Component("customSecurityMetadataSource")
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger logger = Logger.getLogger(CustomSecurityMetadataSource.class);

    public static Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<>();
    private PathMatcher pathMatcher = new AntPathMatcher();
    private String urlroles;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

/*
    public CustomSecurityMetadataSource(*//*String urlroles*//*) {
        super();
        out.print("urlroles is " + urlroles);
        this.urlroles = urlroles;
        resourceMap = loadResourceMatchAuthority();
        resourceMap = loadResourceAndAuthority();
    }*/

    private Map<String, Collection<ConfigAttribute>> loadResourceMatchAuthority() {

        Map<String, Collection<ConfigAttribute>> map = new HashMap<String, Collection<ConfigAttribute>>();

        if (urlroles != null && !urlroles.isEmpty()) {
            String[] resouces = urlroles.split(";");
            for (String resource : resouces) {
                String[] urls = resource.split("=");
                String[] roles = urls[1].split(",");
                Collection<ConfigAttribute> list = new ArrayList<ConfigAttribute>();
                for (String role : roles) {
                    ConfigAttribute config = new SecurityConfig(role.trim());
                    list.add(config);
                }
                //key：url, value：roles
                map.put(urls[0].trim(), list);
            }
        } else {
            logger.error("'securityconfig.urlroles' must be set");
        }

        logger.info("Loaded UrlRoles Resources.");
        return map;
    }

    @PostConstruct
    private void loadResourceAndAuthority() {
     /*   Map<String, Collection<ConfigAttribute>> map = new HashMap<>();*/
/*        RoleRepository roleRepository = SpringUtil.getBean(RoleRepository.class);*/
        List<Role> roleList = roleRepository.findAll();
        for (Role role : roleList) {
            ConfigAttribute ca = new SecurityConfig(role.getName());
            List<Authority> authorities = role.getAuthorities();
            for (Authority authority : authorities) {
                List<Resource> resources = authority.getResources();
                for (Resource resource : resources) {
                    if (resourceMap.containsKey(resource.getResourceName())) {
                        Collection<ConfigAttribute> value = resourceMap.get(resource.getResourceName());
                        value.add(ca);
                        resourceMap.put(resource.getResourceName(), value);
                    } else {
                        Collection<ConfigAttribute> atts = new ArrayList<>();
                        atts.add(ca);
                        resourceMap.put(resource.getResourceName(), atts);
                    }
                }

            }
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        String url = ((FilterInvocation) object).getRequestUrl();

        logger.debug("request url is  " + url);

        if (resourceMap == null)
 /*           resourceMap = loadResourceMatchAuthority();*/
             loadResourceAndAuthority();

        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next();
            if (pathMatcher.match(resURL, url)) {
                return resourceMap.get(resURL);
            }
        }
        return resourceMap.get(url);
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }
}
