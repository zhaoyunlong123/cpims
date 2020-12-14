package com.javakc.springbootcpims.modules.permission.entity;

import com.javakc.springbootcpims.common.base.entity.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: springboot-cpims
 * @Description: RBAC权限模块实体类
 * @Author: zhao yun long
 * @date: 2020/10/31 11:27
 * RBAC：基于角色的绑定控制
 */
@Getter
@Setter
public class Permission extends Base {

    /** 权限名称 */
    private String name;
    /** 权限编号 */
    private String permissionCode;

}
