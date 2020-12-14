package com.javakc.springbootcpims.modules.dictionary.controller;

import com.javakc.springbootcpims.modules.dictionary.entity.Dictionary;
import com.javakc.springbootcpims.modules.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * @program: springboot-cpims
 * @Description: 数据字典接口
 * @Author: zhao yun long
 * @date: 2020/10/22 18:28
 */
@RestController
@RequestMapping("dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    /*数据字典-全部查询*/
    @GetMapping
    public List<Dictionary> query()
    {
        return dictionaryService.queryAll();
    }

    /*数据字典-根据主键id单条查询*/
    @GetMapping("{id}")
    public Dictionary queryById(@PathVariable int id)
    {
        return dictionaryService.queryById(id);
    }

    // 根据子系统名称获取该子系统下所有的数据字典

    // 根据一组分组名称查询所有符合条件的数据字典

    // 根据真实值查询表现值  参数：子系统名称、分组名称、真实值

    /*数据字典-新增单条数据*/
    @PostMapping
    public void insert(@RequestBody Dictionary entity)
    {
        dictionaryService.insert(entity);
    }

    /*数据字典-批量新增*/
    @PostMapping("batch")
    public void inserts(@RequestBody List<Dictionary> list)
    {
        dictionaryService.inserts(list);
    }

    /*数据字典-更改单条数据*/
    @PutMapping
    public void update(@RequestBody Dictionary entity)
    {
        dictionaryService.update(entity);
    }

    /*删除单条记录*/
    @DeleteMapping("{id}")
    public void delete(@PathVariable int id)
    {
        dictionaryService.delete(id);
    }

    /*批量删除*/
    @DeleteMapping
    public void deletes(@RequestBody List<Serializable> ids)
    {
        dictionaryService.deletes(ids);
    }



}
