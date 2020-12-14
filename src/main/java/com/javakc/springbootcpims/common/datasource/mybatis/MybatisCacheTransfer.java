package com.javakc.springbootcpims.common.datasource.mybatis;

import com.javakc.springbootcpims.common.redis.RedisComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @program: spring-boot-ssm
 * @Description: 建立MybatisCache与ioc关联
 * @Author: zhao yun long
 * @date: 2020/10/27 22:00
 * 此对象受ioc管理
 */
@Component
public class MybatisCacheTransfer {

    /**
     * RedisComponent是最原始的方法，RedisConfig是我们封装过的方法
     * @param redisTemplate
     *
     */
    public MybatisCacheTransfer(@Autowired RedisTemplate<String,Object> redisTemplate) {
        MybatisCache.redisTemplate = redisTemplate;
    }
//    public MybatisCacheTransfer(@Autowired RedisComponent redisComponent) {
//        MybatisCache.redisComponent = redisComponent;
//    }

    /**
     * c3p0
     * dbcp/dbcp2
     * hikari(redis默认的数据库连接池)速度快，但功能并不全
     * druid(更改为此数据库连接池)速度稍慢，但功能很齐全
     */

}
