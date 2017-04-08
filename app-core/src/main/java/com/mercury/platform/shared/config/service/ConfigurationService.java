package com.mercury.platform.shared.config.service;


import java.io.IOException;

public interface ConfigurationService<T, K> {
    void load() throws IOException;
    T get(K key);
    void save();
}
