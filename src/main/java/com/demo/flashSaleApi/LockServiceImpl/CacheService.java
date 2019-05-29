package com.demo.flashSaleApi.LockServiceImpl;


import com.demo.flashSaleApi.LockService.ICacheService;
import com.demo.flashSaleApi.util.Constants;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;






@Service(value = "cacheService")
public class CacheService<K, V> implements ICacheService<K, V> {

    private final Map<String, V> LRU_MAP;

    public CacheService() {
        LRU_MAP = Collections.synchronizedMap(new LRUMap<String, V>(100));
    }

    @Autowired
    private RedisTemplate<String, V> redisTemplate;

    @Override
    public V get(final K key, final String prefix) {
        return redisTemplate.opsForValue().get(prefix + ":" + key.toString());
    }

    @Override
    public V getInMemory(K key, String prefix) {
        return LRU_MAP.get(prefix + ":" + key.toString());
    }

    @Override
    public void set(final K key, final String prefix, final V value) {
        this.set(key, prefix, value, Constants.DEFAULT_CACHE_TIMEOUT_IN_SECONDS);
    }

    @Override
    public void setInMemory(K key, String prefix, V value) {
        LRU_MAP.put(prefix + ":" + key.toString(), value);
    }

    @Override
    public void set(final K key, final String prefix, final V value, final Integer timeout) {
        redisTemplate.opsForValue().set(prefix + ":" + key.toString(), value);
        redisTemplate.expire(prefix + ":" + key.toString(), timeout, TimeUnit.SECONDS);
    }

    @Override
    public void delete(final K key, final String prefix) {
        redisTemplate.delete(prefix + ":" + key.toString());
    }

    @Override
    public void deleteInMemory(K key, String prefix) {
        LRU_MAP.remove(prefix + ":" + key.toString());
    }
}


