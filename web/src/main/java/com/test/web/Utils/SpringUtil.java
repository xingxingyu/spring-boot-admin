package com.test.web.Utils;

/**
 * Created by dylan on 2017/9/17.
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import static java.lang.System.out;

/**
 * 普通类调用Spring bean对象：
 * 说明：
 * 1、此类需要放到App.java同包或者子包下才能被扫描，否则失效。
 *
 * @author Administrator
 */

@Component
public class SpringUtil implements ApplicationContextAware, InitializingBean {

    private static ApplicationContext applicationContext = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
        out.println("---------------------------------------------------------------------");
        out.println("---------------------------------------------------------------------");
        out.println("---------------com.kfit.base.util.SpringUtil------------------------------------------------------");
        out.println("========ApplicationContext配置成功,在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象,applicationContext=" + SpringUtil.applicationContext + "========");
        out.println("---------------------------------------------------------------------");

    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);

    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        out.print("1111111111");
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        out.print("开始初始化SpringUtil......");
    }
}