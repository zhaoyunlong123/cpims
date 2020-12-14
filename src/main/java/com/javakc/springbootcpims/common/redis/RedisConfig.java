package com.javakc.springbootcpims.common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: springboot-cpims
 * @Description: 重写RedisTemplate数据封装
 * @Author: zhao yun long
 * @date: 2020/10/24 15:27
 * @Configuration： 相当于ioc中的ConfigurationContext，管理第三方的类
 */
@Configuration
@Order(1)
public class RedisConfig {

    @Bean
    //声明一个全新的RedisTemplate，注入RedisConnectionFactory（redis连接工厂）
    //想要得到redis连接池工厂，就需要地址、端口号等，只要在yml中配置好了，就会去yml中找到文件，创建RedisConnectionFactory
    //RedisConnectionFactory通过读yml文件创建了，然后再注入到这个文件中
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //自己new了一个redisTemplate模板，操作不了数据库，而且与ioc容器没有关系
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 通过setConnectionFactory将redisConnectionFactory连接池工厂注入进来，才算完整的新建完成
        // 执行到这里，跟默认的没有任何区别
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize替换默认(jdkSerialize)序列化  ---  序列化为Json格式
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 跟用Gson相似
        ObjectMapper objectMapper = new ObjectMapper();
        // 解决Java8新日期API序列化问题  解决localDate  localTime
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        // 设置所有访问权限以及所有的实际类型都可序列化和反序列化   @ResponseBody @RequestBody
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}
