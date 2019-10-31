package com.fh.shop.api.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private RedisPool(){

    }
    private static JedisPool jedisPool;

    private static void initPool(){
        //创建对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //设置最大连接数
        jedisPoolConfig.setMaxTotal(1000);
        //设置最大空闲数
        jedisPoolConfig.setMaxIdle(100);
        //设置最小空闲数
        jedisPoolConfig.setMinIdle(100);
        //设置
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestOnBorrow(true);
        //将jedis链接池信息放入JedisPool
        jedisPool = new JedisPool(jedisPoolConfig,"192.168.239.131",7020);
    }
        //静态块  只执行一次
    static {
        initPool();
    }
    //此方法名必须这样写。返回的必须是这个
    public static Jedis getResource(){
        return jedisPool.getResource();
    }
}
