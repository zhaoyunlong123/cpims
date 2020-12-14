package com.javakc.springbootcpims.common.datasource.mybatis;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @program: springboot-cpims
 * @Description: Mybatis二级缓存自定义封装实现
 * @Author: zhao yun long
 * @date: 2020/10/24 17:25
 * mapper中是Mybatis的类型
 */
public class MybatisCache implements Cache {

    /**
     * id:是DictionaryDao.xml中的namespace="com.javakc.springbootcpims.modules.dictionary.dao.DictionaryDao"
     * 当我们实例化对象的时候，new了一个Mybatis，将namespace传进去，当我们用这个类的时候，必须传一个id，确定是哪个模块
     */
    private String id;
    /**
     * 在虚拟机中分配一块静态的内存，存放redisComponent
     */
    public static RedisTemplate<String,Object> redisTemplate;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public MybatisCache(String id) {
        this.id = id;
    }

    /**
     * 通过构造方法将实例化的对象放置到ioc中，
     * 并将DictionaryDao.xml的namespace注入到id中
     * @return
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 向redis中写入查询数据
     * @param key sql的查询语句
     * @param value result结果集
     */
    @Override
    public void putObject(Object key, Object value) {
        //加一个写的锁
        Lock lock = readWriteLock.readLock();
        // 打开锁
        lock.lock();
        //写入操作
        redisTemplate.boundHashOps(id).put(key.toString(),value);
//        System.out.println("是否执行putObject");
        //关闭锁
        lock.unlock();
    }

    /**
     * 从redis中读取数据
     * @param key sql
     * @return result
     */
    @Override
    public Object getObject(Object key) {
        //设置
//        Object object = redisTemplate.boundHashOps(id).get(key.toString());
//        System.out.println( object );
//        return object;
        // 读取
        return redisTemplate.boundHashOps(id).get(key.toString());
//        System.out.println("是否执行getObject");
//        return null;
    }

    /**
     * 从redis中删除数据
     * @param key sql
     * @return true/false
     */
    @Override
    public Object removeObject(Object key) {
        System.out.println("是否执行removeObject");
        return null;
    }

    /**
     * 将redis中的数据清空
     */
    @Override
    public void clear() {

    }

    /**
     * 得到redis的总长度
     * @return 总长度
     */
    @Override
    public int getSize() {
        return 0;
    }

    /**
     * 读写锁
     * @return
     */
    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}
