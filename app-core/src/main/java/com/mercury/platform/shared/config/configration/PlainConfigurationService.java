package com.mercury.platform.shared.config.configration;


public interface PlainConfigurationService<T> {
    T get();

    void set(T descriptor);
}
