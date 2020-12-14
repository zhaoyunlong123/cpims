package com.javakc.springbootcpims.modules.user.entity;

import com.javakc.springbootcpims.common.base.entity.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: springboot-cpims
 * @Description: RBAC用户模块实体类
 * @Author: zhao yun long
 * @date: 2020/10/31 11:26
 * RBAC：基于角色的绑定控制
 */
@Setter
@Getter
public class User extends Base {

    /** 姓名 */
    private String name;
    /** 用户编号 */
    private String userNumber;
    /** 用户状态 */
    private Integer userState;
    /** 登陆名称 */
    private String loginName;
    /** 登陆密码 */
    private String loginPass;
    /** 手机号码 */
    private String phone;
    /** 电子邮箱 */
    private String email;

}
