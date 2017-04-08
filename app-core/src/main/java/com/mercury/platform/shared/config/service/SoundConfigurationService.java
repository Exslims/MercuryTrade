package com.mercury.platform.shared.config.service;

import com.mercury.platform.shared.config.DataSource;
import com.mercury.platform.shared.entity.SoundDescriptor;


public class SoundConfigurationService extends BaseConfigurationService<SoundDescriptor,String> {
    public SoundConfigurationService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void load() {

    }

    @Override
    public SoundDescriptor get(String key) {
        return null;
    }

    @Override
    public void save(SoundDescriptor entity, String key) {

    }
}
