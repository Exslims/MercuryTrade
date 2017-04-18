package com.mercury.platform.shared.config.configration;

import com.mercury.platform.shared.config.DataSource;
import com.mercury.platform.shared.config.JSONHelper;


public abstract class BaseConfigurationService<T,K> implements ConfigurationService<T,K>{
    protected DataSource dataSource;
    protected JSONHelper jsonHelper;

    protected BaseConfigurationService(DataSource dataSource){
        this.dataSource = dataSource;
        this.jsonHelper = new JSONHelper(dataSource);
    }
}
