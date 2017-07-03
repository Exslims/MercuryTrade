package com.mercury.platform.shared.config.configration;


import java.util.Map;

public interface KeyValueConfigurationService<K, T> extends ConfigurationService,HasDefault<Map<String,T>> {
    T get(K key);
    Map<K,T> getMap();
}
