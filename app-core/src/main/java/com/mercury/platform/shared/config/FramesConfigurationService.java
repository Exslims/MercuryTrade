package com.mercury.platform.shared.config;

import com.mercury.platform.shared.entity.FrameSettings;


public class FramesConfigurationService implements ConfigurationService<FrameSettings,String> {
    private BaseDataSource dataSource;
    public FramesConfigurationService(BaseDataSource dataSource) {
        this.dataSource = dataSource;
        load();
    }

    @Override
    public void load() {

    }

    @Override
    public FrameSettings get(String key) {
        return null;
    }

    @Override
    public void save(FrameSettings entity, String key) {

    }
}
