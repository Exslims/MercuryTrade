package com.mercury.platform.shared.config.configration;


public interface PlainConfigurationService<T> extends ConfigurationService, HasDefault<T> {
    T get();
}
