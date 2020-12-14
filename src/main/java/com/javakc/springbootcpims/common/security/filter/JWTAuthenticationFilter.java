package com.javakc.springbootcpims.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javakc.springbootcpims.common.base.response.ResultCode;
import com.javakc.springbootcpims.common.base.response.ResultMessage;
import com.javakc.springbootcpims.common.security.jwt.JsonWebToken;
import com.javakc.springbootcpims.modules.user.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: spring-boot-ssm
 * @Description: 登陆鉴权过滤器
 * @Author: zhao yun long
 * @date: 2020/11/1 16:04
 * 进来后会处理账号密码
 * 这个过滤器只有登录才会走
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JsonWebToken jsonWebToken;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JsonWebToken jsonWebToken) {
        this.authenticationManager = authenticationManager;
        this.jsonWebToken = jsonWebToken;
    }

    /**
     * 收集客户端参数转交security完成认证
     * 收集客户端账号密码信息进行认证
     * @param request  请求
     * @param response 响应
     * @return Authentication           认证
     * @throws AuthenticationException 异常
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //需要用到Jackson转Json，new ObjectMapper承载数据，相当于Gson（作用将Json转对象，将对象转Json）
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //收集客户端的账号密码（从请求中获取输入流，从输入流中得到Json，并将Json封装到User对象中）
            // 由于得到的数据需要和User对象中的字段保持一致，
            // 所以客户端传输的数据的名字必须和User对象的名字保持一致
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            //交由security完成认证
            //第一个参数：明文账号
            //第二个参数：明文密码
            //第三个参数：权限集合（你能做什么，不能做什么）
            //交给认证管理器，认证一下账号密码是否正确
            //查询方式为：select pass from table where name = ？（可以分别判断账号密码）
            //将封装的User对象放到账号密码信息中，加上权限，最终返回一个认证信息对象
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLoginName(), user.getLoginPass(), new ArrayList<>()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }

    /**
     * 登陆成功的回调方法
     * 会生成一个token
     * @param request
     * @param response
     * @param chain
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse
            response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        //1、生成token令牌
        //获取认证成功账号相关信息
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        //向token中添加过期时间
        String token = jsonWebToken.createToken(username, jsonWebToken.getExpiration());

        //登陆认证成功(将生成的token传到客户端)
        ResultMessage.response(response, ResultMessage.success(ResultCode.USER_LOGIN_SUCCESS, token));
    }


    /**
     * 登陆失败的回调方法
     * 返回给前端因为什么失败了
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //账号不存在
        if (failed instanceof UsernameNotFoundException) {
            ResultMessage.response(response, ResultMessage.failure(ResultCode.USER_NOT_EXIST));
        }
        //密码错误
        else if (failed instanceof BadCredentialsException) {
            ResultMessage.response(response, ResultMessage.failure(ResultCode.USER_PASS_ERROR));
        }
        //账号已锁定
        else if (failed instanceof LockedException) {
            ResultMessage.response(response, ResultMessage.failure(ResultCode.USER_ACCOUNT_LOCKED));
        }
        //sql语句错误等其他类型的错误（空指针，越界等）
        else {
            ResultMessage.response(response, ResultMessage.failure(ResultCode.USER_LOGIN_OTHER_ERROR));
        }
    }
}
