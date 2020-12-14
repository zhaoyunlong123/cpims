package com.javakc.springbootcpims.modules.dictionary.init;

import com.javakc.springbootcpims.modules.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: springboot-cpims
 * @Description: 实现缓存预热
 * @Author: zhao yun long
 * @date: 2020/10/28 23:00
 */
@Component
public class InitDictionary {

    public InitDictionary(@Autowired DictionaryService dictionaryService) {
        System.out.println("服务器启动中，完成缓存预热");
        dictionaryService.queryAll();
    }

}
