package com.javakc.springbootcpims.modules.role.controller;

import com.javakc.springbootcpims.modules.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springboot-cpims
 * @Description: 角色模块表现层实现类
 * @Author: zhao yun long
 * @date: 2020/10/31 12:37
 */
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;



}
