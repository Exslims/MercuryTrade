package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.DataSource;
import com.mercury.platform.shared.config.configration.BaseKeyValueConfigurationService;
import com.mercury.platform.shared.entity.FrameSettings;

import java.util.Map;


public class FramesConfigurationService extends BaseKeyValueConfigurationService<FrameSettings,String> {
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
