package com.javakc.springbootcpims.modules.permission.service;

import com.javakc.springbootcpims.common.base.service.BaseService;
import com.javakc.springbootcpims.modules.permission.dao.PermissionDao;
import com.javakc.springbootcpims.modules.permission.entity.Permission;
import org.springframework.stereotype.Service;

/**
 * @program: springboot-cpims
 * @Description: 权限模块逻辑层实现类
 * @Author: zhao yun long
 * @date: 2020/10/31 12:40
 */
@Service
public class PermissionService extends BaseService<PermissionDao, Permission> {
}
