package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.ConfigurationSource;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.entity.FrameSettings;

import java.util.Map;


public class FramesConfigurationService extends BaseConfigurationService implements KeyValueConfigurationService<FrameSettings,String> {
    public FramesConfigurationService(ConfigurationSource dataSource) {
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
    public Map<String, FrameSettings> getMap() {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public FrameSettings getDefault() {
        return null;
    }
}
