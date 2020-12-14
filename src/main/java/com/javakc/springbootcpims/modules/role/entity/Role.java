package com.javakc.springbootcpims.modules.role.entity;

import com.javakc.springbootcpims.common.base.entity.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: springboot-cpims
 * @Description: RBAC角色模块实体类
 * @Author: zhao yun long
 * @date: 2020/10/31 11:26
 * RBAC：基于角色的绑定控制
 */
@Getter
@Setter
public class Role extends Base {

    /** 角色名称 */
    private String name;
    /** 角色编号 */
    private String roleCode;
    /** 角色备注 */
    private String roleReason;

}
