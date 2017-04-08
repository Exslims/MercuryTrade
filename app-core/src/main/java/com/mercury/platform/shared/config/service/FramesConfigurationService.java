package com.mercury.platform.shared.config.service;

import com.mercury.platform.shared.config.DataSource;
import com.mercury.platform.shared.entity.FrameSettings;


public class FramesConfigurationService extends BaseConfigurationService<FrameSettings,String> {
    public FramesConfigurationService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void load() {

    }

    @Override
    public FrameSettings get(String key) {
        return null;
    }

    @Override
    public void save() {

    }
}
