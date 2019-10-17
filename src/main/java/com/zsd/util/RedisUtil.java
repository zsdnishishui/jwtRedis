package com.zsd.util;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
 
    @Autowired
    private StringRedisTemplate redisTemplate; 


	/**
     * 
     * @Title: keys   
     * @Description: TODO(查询key,支持模糊查询，传过来时key的前后端已经加入了*，或者根据具体处理)   
     * @param: @param key
     * @param: @return      
     * @return: Set<String>      
     * @throws
     */
    public Set<String> keys(String key){
        return redisTemplate.keys(key);
    }
 

    /**
     * 
     * @Title: get   
     * @Description: TODO  字符串获取值
     * @param: @param key
     * @param: @return      
     * @return: Object      
     * @throws
     */
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }
 

    /**
     * 
     * @Title: set   
     * @Description: TODO 字符串存入值 默认过期时间为2小时
     * @param: @param key
     * @param: @param value      
     * @return: void      
     * @throws
     */
    public void set(String key, String value){
        redisTemplate.opsForValue().set(key,value,7200,TimeUnit.SECONDS);
    }
 

    /**
     * 
     * @Title: set   
     * @Description: TODO 字符串存入值
     * @param: @param key
     * @param: @param value
     * @param: @param expire  过期时间   （毫秒）
     * @return: void      
     * @throws
     */
    public void set(String key, String value,Integer expire){
        redisTemplate.opsForValue().set(key,value, expire,TimeUnit.SECONDS);
    }
 

    /**
     * 
     * @Title: delete   
     * @Description: TODO  删出key
     * @param: @param key      
     * @return: void      
     * @throws
     */
    public void delete(String key){
        redisTemplate.opsForValue().getOperations().delete(key);
    }
 
    /**
     * 
     * @param key    key
     * @param filed  filed
     * @param domain 对象
     */
    /**
     * 
     * @Title: hset   
     * @Description: TODO 添加单个 默认过期时间为两小时
     * @param: @param key
     * @param: @param filed
     * @param: @param domain      
     * @return: void      
     * @throws
     */
    public void hset(String key,String filed,Object domain){
        redisTemplate.opsForHash().put(key, filed, domain);
    }
 
    /**
     * 
     * @Title: hset   
     * @Description: TODO * 添加单个 
     * @param: @param key
     * @param: @param filed
     * @param: @param domain 对象
     * @param: @param expire   过期时间（毫秒计）    
     * @return: void      
     * @throws
     */
    public void hset(String key,String filed,Object domain,Integer expire){
        redisTemplate.opsForHash().put(key, filed, domain);
        redisTemplate.expire(key, expire,TimeUnit.SECONDS);
    }
 
    /**
     * 
     * @Title: hset   
     * @Description: TODO 添加HashMap
     * @param: @param key
     * @param: @param hm     要存入的hash表 
     * @return: void      
     * @throws
     */
    public void hset(String key, HashMap<String,Object> hm){
        redisTemplate.opsForHash().putAll(key,hm);
    }
 

    /**
     * 
     * @Title: hsetAbsent   
     * @Description: TODO  如果key存在就不覆盖
     * @param: @param key
     * @param: @param filed
     * @param: @param domain      
     * @return: void      
     * @throws
     */
    public void hsetAbsent(String key,String filed,Object domain){
        redisTemplate.opsForHash().putIfAbsent(key, filed, domain);
    }

    /**
     * 
     * @Title: hget   
     * @Description: TODO 查询key和field所确定的值
     * @param: @param key 查询的key
     * @param: @param field 查询的field
     * @param: @return      
     * @return: Object      
     * @throws
     */
    public Object hget(String key,String field) {
        return redisTemplate.opsForHash().get(key, field);
    }
 
    /**
     * 
     * @Title: hget   
     * @Description: TODO 查询该key下所有值
     * @param: @param key 查询的key
     * @param: @return   Map<HK, HV>   
     * @return: Object      
     * @throws
     */
    public Object hget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
 
    /**
     * 
     * @Title: deleteKey   
     * @Description: TODO * 删除key下所有值
     * @param: @param key 查询的key    
     * @return: void      
     * @throws
     */
    public void deleteKey(String key) {
        redisTemplate.opsForHash().getOperations().delete(key);
    }
 
    /**
     * 
     * @Title: hasKey   
     * @Description: TODO  判断key和field下是否有值
     * @param: @param  key 判断的key
     * @param: @param  field 判断的field
     * @param: @return      
     * @return: Boolean      
     * @throws
     */
    public Boolean hasKey(String key,String field) {
    	try {
           return redisTemplate.opsForHash().hasKey(key,field);
    	}finally {
    	   //手动释放资源
           RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
    	}
    }
 

    /**
     * 
     * @Title: hasKey   
     * @Description: TODO 判断key下是否有值
     * @param: @param key 判断的key
     * @param: @return      
     * @return: Boolean      
     * @throws
     */
    public Boolean hasKey(String key) {
    	try {
        return redisTemplate.opsForHash().getOperations().hasKey(key);
        }finally {
 	   //手动释放资源
        RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
 	    }
    }
 

    /**
     * 
     * @Title: isBlackList   
     * @Description: TODO 判断此token是否在黑名单中
     * @param: @param token
     * @param: @return      
     * @return: Boolean      
     * @throws
     */
    public Boolean isBlackList(String token){
        return hasKey("blacklist",token);
    }

    /**
     * 
     * @Title: addBlackList   
     * @Description: TODO  将token加入到redis黑名单中
     * @param: @param token      
     * @return: void      
     * @throws
     */
    public void addBlackList(String token){
        hset("blacklist", token,"true");
    }
 
 

    /**
     * 
     * @Title: getTokenValidTimeByToken   
     * @Description: TODO 查询token下的刷新时间
     * @param: @param token 查询的key
     * @param: @return      
     * @return: Object      
     * @throws
     */
    public Object getTokenValidTimeByToken(String token) {
        return redisTemplate.opsForHash().get(token, "tokenValidTime");
    }
 
    /**
     * 
     * @Title: getUsernameByToken   
     * @Description: TODO 查询token下的刷新时间
     * @param: @param  token 查询的key
     * @param: @return    HV   
     * @return: Object      
     * @throws
     */
    public Object getUsernameByToken(String token) {
        return redisTemplate.opsForHash().get(token, "username");
    }
 

    /**
     * 
     * @Title: getIPByToken   
     * @Description: TODO * 查询token下的刷新时间
     * @param: @param token 查询的key
     * @param: @return  HV    
     * @return: Object      
     * @throws
     */
    public Object getIPByToken(String token) {
        return redisTemplate.opsForHash().get(token, "ip");
    }
 
    /**
     * 
     * @Title: getExpirationTimeByToken   
     * @Description: TODO 查询token下的过期时间
     * @param: @param token 查询的key
     * @param: @return      
     * @return: Object      
     * @throws
     */
    public Object getExpirationTimeByToken(String token) {
        return redisTemplate.opsForHash().get(token, "expirationTime");
    }


}
