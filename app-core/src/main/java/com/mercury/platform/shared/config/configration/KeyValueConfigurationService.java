package com.mercury.platform.shared.config.configration;


import java.util.Map;

public interface KeyValueConfigurationService<K, T> {
    T get(K key);

    Map<K, T> getMap();

    void set(Map<K, T> map);
}
