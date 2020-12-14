package com.javakc.springbootcpims.common.security;

import com.javakc.springbootcpims.common.base.response.ResultCode;
import com.javakc.springbootcpims.common.base.response.ResultMessage;
import com.javakc.springbootcpims.common.security.filter.JWTAuthenticationFilter;
import com.javakc.springbootcpims.common.security.filter.JWTAuthorizationFilter;
import com.javakc.springbootcpims.common.security.jwt.JsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @program: springboot-cpims
 * @Description: 处理项目安全认证管理
 * @Author: zhao yun long
 * @date: 2020/10/31 16:36
 */
// 实例化里边的内容
// Web application == 项目 springboot-cpims
// 针对于这个项目的安全配置
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 密码加密管理
     * @return BCryptPasswordEncoder
     * 这是security提供的密码加密器管理对象，通过这个加密器进行加密
     * 首先把这个对象交由ioc容器管理（Encoder：加密；Decoder：解密）
     */
    @Bean("passwordEncode")
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /*
     * 根据登陆的用户名
     * 查询用户详情实现
     * 直接通过id装配，防止出现多个实现类，从而导致装配失败
     */
    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    @Qualifier("JsonWebToken")
    private JsonWebToken jsonWebToken;

    /**
     * 自定义处理UserNotFoundExceptions
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //不要隐藏用户找不到异常（默认为true）
        provider.setHideUserNotFoundExceptions(false);
        //经过JWTAuthenticationFilter认证完成后，用下边的方法查询数据库
        //根据登录账号查询用户信息
        provider.setUserDetailsService(userDetailsService);
        //如果用户存在则匹配密码
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    /**
     * 认证管理器
     * @param auth 认证管理构建
     * @throws Exception 异常
     * Authentication：认证，鉴权
     * Manager：管理器
     * Builder：构建
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
         * inMemory：内存
         * 在内存中虚拟出来一个用户
         * spring security
         *   默认的账号user密码3be533c2-5fb1-411c-96e7-c4b29010010b
         *   向内存中模拟账号admin密码123456
         */
//        auth.inMemoryAuthentication().withUser("admin").password("123456");

//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * cors：跨域（域：应用application，一个项目由很多个应用组成，
         * 应用A访问应用B，这就属于跨域）如果把跨域开了会影响调用，
         * 如果把跨域关了，就会产生任何一个应用都可以调用本应用，所以就需要对跨域进行一个管理
         */
        http.cors();
        /*
         * 当前Spring Security使用JWT（JSON WEB TOKEN）实现
         * 关闭Spring Security csrf(跨站点请求伪造)
         */
        http.csrf().disable();
        /*
         * spring security会话管理
         * SessionCreationPolicy.ALWAYS         一直创建HttpSession
         * SessionCreationPolicy.NEVER          Spring Security不会创建HttpSession，但如果它已经存在则使用HttpSession
         * SessionCreationPolicy.IF_REQUIRED    Spring Security只会在需要时创建一个HttpSession
         * SessionCreationPolicy.STATELESS      Spring Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
         * 项目发布后，随着访问的递增，并发量的增加，一直到这一个应用扛不住为止，
         * 一定会考虑一个事情就是负载（多个应用分散抗压）均衡，
         * 如果用的是session则会产生从A处登录时创建一个会话，再去另B访问，
         * 在B处重新登录再创建一个会话，而这两个会话是不同的，无法达到在两个应用中随意转换
         * 结果就是走到哪里登陆到哪里，十分不方便
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /*
         * Spring Security配置放行与拦截规则
         * 【系统管理】【用户注册】 /system/user 放行
         * 【其余请求】 需要认证
         * 1、用户注册放行
         *      如果拦截到了user请求则直接放行（请求的方式为POST）
         *      antMatcher：匹配；permitAll：放行全部
         * 2、swagger放行
         * 3、druid放行
         */
        http.authorizeRequests()
                //用户注册放行
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                //swagger放行
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v3/api-docs").permitAll()
                //druid放行
                .antMatchers("/druid/**").permitAll()
                //除了以上，其他的都需要认证
                .anyRequest().authenticated();
        /**
         * exceptionHandling：当请求出现异常的处理
         * authenticationEntryPoint：
         * accessDeniedHandler：当请求出现拒绝访问的处理
         */
        http
            .exceptionHandling()
            //重写用户未登录异常
            .authenticationEntryPoint((request, response, exception) -> {
                ResultMessage.response(response, ResultMessage.failure(ResultCode.USER_NOT_LOGIN));
            })
            //重写拒绝访问的异常
            .accessDeniedHandler((request, response, exception) -> {
                //1、把对象转为Json  2、把Json传到页面
                ResultMessage.response(response, ResultMessage.failure(ResultCode.PERMISSION_AAccess_Denied));
            })
            .and()
            .logout()
            .logoutSuccessHandler((request, response, exception) -> {
                /**
                 * session + cookie
                 * 退出: 通过销毁服务器端的session
                 *
                 * token
                 * 退出: ??? 客户端token 服务器管理token
                 */
                ResultMessage.response(response, ResultMessage.failure(ResultCode.USER_LOGOUT_SUCCESS));
            });

        /*
         * Spring Security配置过滤器（编码过滤器，登陆过滤器，自动过滤器）
         * JWTAuthenticationFilter(authenticationManager():认证管理器) 登陆认证过滤器
         * JWTAuthorizationFilter 权限授权过滤器
         */
        http
            .addFilter(new JWTAuthenticationFilter(authenticationManager(),jsonWebToken))
            .addFilter(new JWTAuthorizationFilter(authenticationManager(),jsonWebToken));
    }


}
