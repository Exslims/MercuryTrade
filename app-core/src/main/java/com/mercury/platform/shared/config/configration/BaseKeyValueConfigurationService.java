package com.mercury.platform.shared.config.configration;

import com.mercury.platform.shared.config.DataSource;
import com.mercury.platform.shared.config.JSONHelper;


public abstract class BaseKeyValueConfigurationService<T,K> implements KeyValueConfigurationService<T,K> {
    protected DataSource dataSource;
    protected JSONHelper jsonHelper;

    protected BaseKeyValueConfigurationService(DataSource dataSource){
        this.dataSource = dataSource;
        this.jsonHelper = new JSONHelper(dataSource);
    }
}
