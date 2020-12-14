package com.javakc.springbootcpims.common.security.filter;

import com.javakc.springbootcpims.common.security.jwt.JsonWebToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: springboot-cpims
 * @Description: 权限授权过滤器
 * @Author: zhao yun long
 * @date: 2020/11/3 16:36
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JsonWebToken jsonWebToken;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JsonWebToken jsonWebToken) {
        super(authenticationManager);
        this.jsonWebToken = jsonWebToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 1、从请求中获取token
        String token = jsonWebToken.getToken(request);
        //1.1如果无法从请求中获取token，则说明未登录
        //判断token是否为空，因为不一定会从request中得到token
        //请求头中没有Authorization则直接放行
        //结果就是通不过，会直接return返回，不会再继续往下走
        if (StringUtils.isEmpty(token)) {
            //执行放行，说明没有授权
            chain.doFilter(request, response);
            return;
        }
        //1.2验证token是否过期

        //请求头中有token则进行解析并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(token, request, response));
        super.doFilterInternal(request, response, chain);

    }

    /**
     * 账号密码认证token
     * @param token 请求携带token
     * @return 账号密码认证token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token, HttpServletRequest request, HttpServletResponse response) {
        //replace:替换  Prefix：application.yml中的prefix: "Bearer "
        //这句是截取prefix，大前边已经截取过了，所以这步可以省略
//        token = token.replace(jsonWebToken.getPrefix(), "");
        String username = jsonWebToken.getUsername(token);
        if (!StringUtils.isEmpty(username)){
            return new UsernamePasswordAuthenticationToken(username, null, jsonWebToken.getAuthenticationFromToken(request, response).getAuthorities());
        }
        return null;
    }
}
