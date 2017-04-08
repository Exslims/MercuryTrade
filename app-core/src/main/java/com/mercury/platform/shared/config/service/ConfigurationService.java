package com.mercury.platform.shared.config.service;


public interface ConfigurationService<T, K> {
    void load();
    T get(K key);
    void save(T entity, K key);
}
