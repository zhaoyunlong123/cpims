package com.javakc.springbootcpims.common.security.service;

import com.javakc.springbootcpims.modules.user.dao.UserDao;
import com.javakc.springbootcpims.modules.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: springboot-cpims
 * @Description: 执行登录的查询相关功能
 * @Author: zhao yun long、
 */
@Service("userDetailsService")
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    /**
     * 根据登陆名称匹配用户对象信息
     * @param username 登陆账号
     * @return spring security封装用户对象
     * loadUserByUsername:通过账号装载查询用户对象（只用传入账号就可以了）
     * 如果返回为null：提示账号不存在
     * 如果账号存在，密码不对：提示密码不匹配
     * 如果账号密码都正确，还需要判断user_state状态
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);
        if(StringUtils.isEmpty(user))
        {
            throw new UsernameNotFoundException("很遗憾，账号不存在！");
        }
        else if(user.getUserState() == 1)
        {
            throw new LockedException("很遗憾，账号已锁定");
        }
        //查询用户权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        //上边用了user下边再次调用，需要写全路径;刚才查数据库的时候没有返回账号，所以直接用上边的参数中的username
        //最后的验证密码是由这部分做的，不需要自己来做
        return new org.springframework.security.core.userdetails.User(username, user.getLoginPass(), authorities);
    }
}
