package com.fh.shop.api.util;

import redis.clients.jedis.Jedis;

public class RedisUtil {

    public static void set (String key, String value){
        Jedis resource = null;
        try {
            resource = RedisPool.getResource();
            resource.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource != null){
                resource.close();
            }
        }
    }
    public static Long del(String key){
        Jedis resource = null;
        Long del = 0L;
        try {
            resource = RedisPool.getResource();
            del = resource.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource != null){
                resource.close();
            }
        }
        return del;
    }
    public static void delBatch(String... keys){
        Jedis resource = null;
        try {
            resource = RedisPool.getResource();
            resource.del(keys);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource != null){
                resource.close();
            }
        }
    }
    public static void expire(String key,int seconds){
        Jedis resource = null;
        try {
            resource = RedisPool.getResource();
            resource.expire(key,seconds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resource != null){
                resource.close();
            }
        }
    }
    public static String get(String key){
        Jedis resource = null;
        String redisValue = null;
        try {
            resource = RedisPool.getResource();
            redisValue = resource.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource != null){
                resource.close();
            }
        }
        return redisValue;
    }
    public static void setEx(String key,int seconds,String value){
        Jedis resource = null;
        try {
            resource = RedisPool.getResource();
            resource.setex(key,seconds,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource != null){
                resource.close();
            }
        }
    }
    public static boolean exists(String key){
        Jedis resource = null;
        boolean exist = false;
        try {
            resource = RedisPool.getResource();
            exist = resource.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != resource){
                resource.close();
            }
        }
        return exist;
    }

    public static String hget(String key,String filed){
        Jedis resource = null;
        String redisValue = null;
        try {
            resource = RedisPool.getResource();
            redisValue = resource.hget(key,filed);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource != null){
                resource.close();
            }
        }
        return redisValue;
    }

    public static void hset(String key,String filed,String value){
        Jedis resource = null;

        try {
            resource = RedisPool.getResource();
            resource.hset(key,filed,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource != null){
                resource.close();
            }
        }

    }

    public static void hdel(String key,String filed){
        Jedis resource = null;

        try {
            resource = RedisPool.getResource();
            resource.hdel(key,filed);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource != null){
                resource.close();
            }
        }

    }
}
