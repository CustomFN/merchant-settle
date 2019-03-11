package com.z.merchantsettle.utils.redis;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class RedisHelperImpl<HK, T> implements RedisHelper<HK, T>{


    @Autowired
    private RedisTemplate<String, T> redisTemplate;
    @Autowired
    private HashOperations<String, HK, T> hashOperations;
    @Autowired
    private ListOperations<String, T> listOperations;
    @Autowired
    private ZSetOperations<String, T> zSetOperations;
    @Autowired
    private SetOperations<String, T> setOperations;
    @Autowired
    private ValueOperations<String, T> valueOperations;


    @Override
    public void hashPut(String key, HK hashKey, T domain) {
        hashOperations.put(key, hashKey, domain);
    }

    @Override
    public Map<HK, T> hashFindAll(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public T hashGet(String key, HK hashKey) {
        return hashOperations.get(key, hashKey);
    }

    @Override
    public void hashRemove(String key, HK hashKey) {
        hashOperations.delete(key, hashKey);
    }

    @Override
    public Long listRightPush(String key, T domain) {
        return listOperations.rightPush(key, domain);
    }

    @Override
    public Long listLeftPush(String key, T domain) {
        return listOperations.leftPush(key, domain);
    }

    @Override
    public List<T> listFindAll(String key) {
        if (!redisTemplate.hasKey(key)) {
            return Lists.newArrayList();
        }
        return listOperations.range(key, 0, listOperations.size(key));
    }

    @Override
    public T listLeftPop(String key) {
        return listOperations.leftPop(key);
    }

    @Override
    public void valuePut(String key, T domain) {
        valueOperations.set(key, domain);
    }

    @Override
    public T getValue(String key) {
        return valueOperations.get(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean expirse(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }
}
