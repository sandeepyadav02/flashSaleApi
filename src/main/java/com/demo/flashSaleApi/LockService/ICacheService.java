package com.demo.flashSaleApi.LockService;

public interface ICacheService <K,V>{

    V get(final K key, final String prefix);

    V getInMemory(final K key, final String prefix);

    void set(final K key, final String prefix, final V value);

    void setInMemory(final K key, final String prefix, final V value);

    void set(final K key, final String prefix, final V value, final Integer timeout);

    void delete(final K key, final String prefix);

    void deleteInMemory(final K key, final String prefix);
}
