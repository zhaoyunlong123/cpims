package com.javakc.springbootcpims.common.base.service;

import com.javakc.springbootcpims.common.base.dao.BaseDao;
import com.javakc.springbootcpims.common.base.entity.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @program: spring-boot-ssm
 * @Description: 封装逻辑层实现(抽象类，只提供方法，不做实现)
 * @Author: zhao yun long
 * @date: 2020/10/22 17:43
 * 由于逻辑层需要注入自己的数据层,直接将需要注入的数据层放在泛型中,
 * 在公共的逻辑层实现中将其注入到逻辑层(做装配，做注入)，
 * 这就是对于逻辑层实现类的封装
 * D extends BaseDao,T extends Base：将对应所需要的数据进行继承
 */
@Transactional(readOnly = true)
public abstract class BaseService<D extends BaseDao,T extends Base> {

    @Autowired
    private D dao;

    /**
     * 单条插入数据
     * @param entity 实体类
     * @return 条数
     */
    @Transactional(readOnly = false)
    public int insert(T entity)
    {
        return dao.insert(entity);
    }

    /**
     * 批量添加
     * @param list 实体类集合
     * @return 条数
     */
    @Transactional(readOnly = false)
    public int inserts(List<T> list)
    {
        return dao.inserts(list);
    }

    /**
     * 单条修改
     * @param entity 实体类
     * @return 条数
     */
    @Transactional(readOnly = false)
    public int update(T entity)
    {
        return dao.update(entity);
    }

    /**
     * 根据条件修改
     * @param params 动态条件
     * @return 条数
     */
    @Transactional(readOnly = false)
    public int updateByParams(Map<String,Object> params)
    {
        return dao.updateByParams(params);
    }

    /**
     * 根据主键单条删除
     * @param id 主键id
     * @return 条数
     */
    @Transactional(readOnly = false)
    public int delete(java.io.Serializable id)
    {
        return dao.delete(id);
    }

    /**
     * 根据主键批量删除
     * @param list 主键集合
     * @return 条数
     */
    @Transactional(readOnly = false)
    public int deletes(List<Serializable> list)
    {
        return dao.deletes(list);
    }

    /**
     * 根据动态条件删除数据
     * @param params 动态条件
     * @return 条数
     */
    @Transactional(readOnly = false)
    public int deleteByParams(Map<String,Object> params)
    {
        return dao.deleteByParams(params);
    }

    /**
     * 清空当前库
     * @return 条数
     * @Deprecated：过时的
     */
    @Deprecated
    @Transactional(readOnly = false)
    public int deleteAll()
    {
        return dao.deleteAll();
    }

    /**
     * 根据主键查询单个对象
     * @param id 主键id
     * @return 对象
     */
    public T queryById(java.io.Serializable id)
    {
        return (T) dao.queryById(id);
    }

    /**
     * 根据动态条件查询单个对象
     * @param params 动态条件
     * @return 对象
     */
    public T queryEntityByParams(Map<String,Object> params)
    {
        return (T) dao.queryEntityByParams(params);
    }

    /**
     * 查询全部数据
     * @return 对象集合
     * @Deprecated：过时的
     */
    @Deprecated
    public List<T> queryAll()
    {
        return dao.queryAll();
    }

    /**
     * 根据动态条件查询对象集合
     * @param params
     * @return
     */
    public List<T> queryByParams(Map<String,Object> params)
    {
        return dao.queryByParams(params);
    }

    /**
     * 根据动态条件查询总条数
     * @param params 动态条件
     * @return 总条数
     */
    public long queryByCountParams(Map<String,Object> params)
    {
        return dao.queryByCountParams(params);
    }



}
