package com.mercury.platform.shared.config.configration;


public interface HasDefault<T> {
    T getDefault();

    void toDefault();
}
