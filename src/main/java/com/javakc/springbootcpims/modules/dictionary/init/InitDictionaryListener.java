package com.javakc.springbootcpims.modules.dictionary.entity;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @program: spring-boot-ssm
 * @Description: 缓存预热监听器
 * @Author: zhao yun long
 * @date: 2020/10/29 9:50
 */
@WebListener
public class InitDictionaryListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("监听服务器启动");
    }

}
