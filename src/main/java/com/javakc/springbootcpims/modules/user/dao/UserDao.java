package com.javakc.springbootcpims.modules.user.dao;

import com.javakc.springbootcpims.common.base.dao.BaseDao;
import com.javakc.springbootcpims.modules.user.entity.User;

/**
 * @program: springboot-cpims
 * @Description: 用户模块数据层接口
 * @Author: zhao yun long
 * @date: 2020/10/31 12:41
 */
public interface UserDao extends BaseDao<User,Integer> {

    public User loadUserByUsername(String username);

}
