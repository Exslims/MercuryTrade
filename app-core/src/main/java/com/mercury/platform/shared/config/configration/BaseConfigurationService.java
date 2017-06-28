package com.mercury.platform.shared.config.configration;

import com.mercury.platform.shared.config.ConfigurationSource;
import com.mercury.platform.shared.config.JSONHelper;


public abstract class BaseConfigurationService<T> {
    protected ConfigurationSource dataSource;
    protected JSONHelper jsonHelper;
    protected T data;

    protected BaseConfigurationService(ConfigurationSource dataSource){
        this.dataSource = dataSource;
        this.jsonHelper = new JSONHelper(dataSource);
    }
}
