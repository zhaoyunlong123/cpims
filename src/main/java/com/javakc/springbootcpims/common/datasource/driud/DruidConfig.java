package com.javakc.springbootcpims.common.datasource.driud;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: springboot-cpims
 * @description: druid数据源监控
 * @author: zhou hong gang
 * @create: 2020-10-20 10:19
 */
@Configuration
public class DruidConfig {

    /**
     * ServletRegistrationBean 注册servlet
     * @return ServletRegistrationBean ：把一个servlet注册到ioc容器
     * 功能：如何注册一个servlet，自己写一个servlet，继承httpServlet，再将这个servlet注册一下，就可以通过地址栏访问了
     * 泛型中的内容就是自己要注册的内容，
     * new后边括号中的为拦截地址：包含"/druid/*"的统一进行拦截，拦截下来进行登录，这也就是url访问的时候为什么以druid结尾
     * 属于ioc和listener之间的关系（两者是平级的关系，只是建立起两者之间的联系），而MybatisCache是ioc内部的关系（属于mybatis自己new的对象未到放入到ioc容器中），
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
        //初始化登陆参数
        Map<String,String> initParams = new HashMap<>();
        //访问账号
        initParams.put("loginUsername", "admin");
        //访问密码
        initParams.put("loginPassword", "123456");
        /*
         * allow: ip白名单
         *    initParas.put("allow",""); 这个值为空或没有就允许所有人访问，ip白名单
         *    initParas.put("allow","localhost");  只允许本机访问，多个ip用逗号,隔开
         * deny: ip黑名单
         *    initParas.put("deny","192.168.1.109"); 拒绝192.168.1.109访问
         *
         * 如果deny和allow同时存在优先deny
         */
        initParams.put("allow", "");
        //禁用HTML页面的Reset按钮
        initParams.put("resetEnable", "false");
        bean.setInitParameters(initParams);
        return bean;
    }

    /**
     * FilterRegistrationBean 注册filter
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> webStatFilter(){
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        //设置阿里巴巴的过滤器
        bean.setFilter(new WebStatFilter());
        bean.addUrlPatterns("/*");

        //把不需要监控的过滤掉,这些不进行统计
        Map<String,String> initParams = new HashMap<>();
        //exclusions:排除在外的，因为druid只需要拦截与数据库相关的
        initParams.put("exclusions","*.js,*.css,*.jpg,/druid/*");
        bean.setInitParameters(initParams);
        return bean;
    }

}
