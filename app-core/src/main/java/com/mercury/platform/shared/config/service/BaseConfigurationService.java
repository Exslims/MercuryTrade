package com.mercury.platform.shared.config.service;

import com.mercury.platform.shared.config.DataSource;

/**
 * Created by Константин on 08.04.2017.
 */
public abstract class BaseConfigurationService<T,K> implements ConfigurationService<T,K>{
    private DataSource dataSource;
    protected BaseConfigurationService(DataSource dataSource){
        this.dataSource = dataSource;
    }
}
