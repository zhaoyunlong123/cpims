package com.javakc.springbootcpims.common.base.dao;

import java.util.List;
import java.util.Map;
/**
 * @program: springboot-cpims
 * @Description: 提供所有模块公共的抽象方法
 * @Author: zhao yun long
 * @date: 2020/10/22 17:41
 * @param <T>   当前模块实体类
 * @param <ID>  当前模块主键类型
 */
public interface BaseDao<T, ID> {
    /**
     * 单条插入数据
     * @param entity 实体类
     * @return 条数
     */
    int insert(T entity);

    /**
     * 批量添加
     * @param list 实体类集合
     * @return 条数
     */
    int inserts(List<T> list);

    /**
     * 单条修改
     * @param entity 实体类
     * @return 条数
     */
    int update(T entity);

    /**
     * 根据条件修改
     * @param params 动态条件
     * @return 条数
     */
    int updateByParams(Map<String,Object> params);

    /**
     * 根据主键单条删除
     * @param id 主键id
     * @return 条数
     */
    int delete(ID id);

    /**
     * 根据主键批量删除
     * @param list 主键集合
     * @return 条数
     */
    int deletes(List<ID> list);

    /**
     * 根据动态条件删除数据
     * @param params 动态条件
     * @return 条数
     */
    int deleteByParams(Map<String,Object> params);

    /**
     * 清空当前库
     * @return 条数
     * @Deprecated：过时的
     */
    @Deprecated
    int deleteAll();

    /**
     * 根据主键查询单个对象
     * @param id 主键id
     * @return 对象
     */
    T queryById(ID id);

    /**
     * 根据动态条件查询单个对象
     * @param params 动态条件
     * @return 对象
     */
    T queryEntityByParams(Map<String,Object> params);

    /**
     * 查询全部数据
     * @return 对象集合
     * @Deprecated：过时的
     */
    @Deprecated
    List<T> queryAll();

    /**
     * 根据动态条件查询对象集合
     * @param params
     * @return
     */
    List<T> queryByParams(Map<String,Object> params);

    /**
     * 根据动态条件查询总条数
     * @param params 动态条件
     * @return 总条数
     */
    long queryByCountParams(Map<String,Object> params);

}
