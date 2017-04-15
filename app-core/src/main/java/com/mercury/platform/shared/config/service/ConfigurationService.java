package com.mercury.platform.shared.config.service;


import java.io.IOException;
import java.util.Map;

public interface ConfigurationService<T, K> {
    void load() throws IOException;
    T get(K key);
    Map<K,T> getMap();
    void save();
}
