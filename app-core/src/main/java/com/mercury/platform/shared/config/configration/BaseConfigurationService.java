package com.mercury.platform.shared.config.configration;

import com.mercury.platform.shared.config.ConfigurationSource;
import com.mercury.platform.shared.config.JSONHelper;


public abstract class BaseConfigurationService {
    protected ConfigurationSource dataSource;
    protected JSONHelper jsonHelper;

    protected BaseConfigurationService(ConfigurationSource dataSource){
        this.dataSource = dataSource;
        this.jsonHelper = new JSONHelper(dataSource);
    }
}
